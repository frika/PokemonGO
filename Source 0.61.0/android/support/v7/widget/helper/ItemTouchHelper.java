package android.support.v7.widget.helper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.animation.AnimatorListenerCompat;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.C0123R;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ChildDrawingOrderCallback;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import com.google.android.gms.location.LocationStatusCodes;
import com.mopub.volley.DefaultRetryPolicy;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import java.util.ArrayList;
import java.util.List;
import spacemadness.com.lunarconsole.C1628R;

public class ItemTouchHelper extends ItemDecoration implements OnChildAttachStateChangeListener {
    private static final int ACTION_MODE_DRAG_MASK = 16711680;
    private static final int ACTION_MODE_IDLE_MASK = 255;
    private static final int ACTION_MODE_SWIPE_MASK = 65280;
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;
    private static final int ACTIVE_POINTER_ID_NONE = -1;
    public static final int ANIMATION_TYPE_DRAG = 8;
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
    private static final boolean DEBUG = false;
    private static final int DIRECTION_FLAG_COUNT = 8;
    public static final int DOWN = 2;
    public static final int END = 32;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int START = 16;
    private static final String TAG = "ItemTouchHelper";
    public static final int UP = 1;
    int mActionState;
    int mActivePointerId;
    Callback mCallback;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    private GestureDetectorCompat mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    private final OnItemTouchListener mOnItemTouchListener;
    private View mOverdrawChild;
    private int mOverdrawChildPosition;
    final List<View> mPendingCleanup;
    List<RecoverAnimation> mRecoverAnimations;
    private RecyclerView mRecyclerView;
    private final Runnable mScrollRunnable;
    ViewHolder mSelected;
    int mSelectedFlags;
    float mSelectedStartX;
    float mSelectedStartY;
    private int mSlop;
    private List<ViewHolder> mSwapTargets;
    private final float[] mTmpPosition;
    private Rect mTmpRect;
    private VelocityTracker mVelocityTracker;

    public interface ViewDropHandler {
        void prepareForDrop(View view, View view2, int i, int i2);
    }

    /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.1 */
    class C01541 implements Runnable {
        C01541() {
        }

        public void run() {
            if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
                if (ItemTouchHelper.this.mSelected != null) {
                    ItemTouchHelper.this.moveIfNecessary(ItemTouchHelper.this.mSelected);
                }
                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                ViewCompat.postOnAnimation(ItemTouchHelper.this.mRecyclerView, this);
            }
        }
    }

    /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.2 */
    class C01552 implements OnItemTouchListener {
        C01552() {
        }

        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(event);
            int action = MotionEventCompat.getActionMasked(event);
            if (action == 0) {
                ItemTouchHelper.this.mActivePointerId = MotionEventCompat.getPointerId(event, ItemTouchHelper.ACTION_STATE_IDLE);
                ItemTouchHelper.this.mInitialTouchX = event.getX();
                ItemTouchHelper.this.mInitialTouchY = event.getY();
                ItemTouchHelper.this.obtainVelocityTracker();
                if (ItemTouchHelper.this.mSelected == null) {
                    RecoverAnimation animation = ItemTouchHelper.this.findAnimation(event);
                    if (animation != null) {
                        ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                        itemTouchHelper.mInitialTouchX -= animation.mX;
                        itemTouchHelper = ItemTouchHelper.this;
                        itemTouchHelper.mInitialTouchY -= animation.mY;
                        ItemTouchHelper.this.endRecoverAnimation(animation.mViewHolder, true);
                        if (ItemTouchHelper.this.mPendingCleanup.remove(animation.mViewHolder.itemView)) {
                            ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, animation.mViewHolder);
                        }
                        ItemTouchHelper.this.select(animation.mViewHolder, animation.mActionState);
                        ItemTouchHelper.this.updateDxDy(event, ItemTouchHelper.this.mSelectedFlags, ItemTouchHelper.ACTION_STATE_IDLE);
                    }
                }
            } else if (action == 3 || action == ItemTouchHelper.UP) {
                ItemTouchHelper.this.mActivePointerId = ItemTouchHelper.ACTIVE_POINTER_ID_NONE;
                ItemTouchHelper.this.select(null, ItemTouchHelper.ACTION_STATE_IDLE);
            } else if (ItemTouchHelper.this.mActivePointerId != ItemTouchHelper.ACTIVE_POINTER_ID_NONE) {
                int index = MotionEventCompat.findPointerIndex(event, ItemTouchHelper.this.mActivePointerId);
                if (index >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(action, event, index);
                }
            }
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(event);
            }
            if (ItemTouchHelper.this.mSelected != null) {
                return true;
            }
            return ItemTouchHelper.DEBUG;
        }

        public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            int newPointerIndex = ItemTouchHelper.ACTION_STATE_IDLE;
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(event);
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(event);
            }
            if (ItemTouchHelper.this.mActivePointerId != ItemTouchHelper.ACTIVE_POINTER_ID_NONE) {
                int action = MotionEventCompat.getActionMasked(event);
                int activePointerIndex = MotionEventCompat.findPointerIndex(event, ItemTouchHelper.this.mActivePointerId);
                if (activePointerIndex >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(action, event, activePointerIndex);
                }
                ViewHolder viewHolder = ItemTouchHelper.this.mSelected;
                if (viewHolder != null) {
                    switch (action) {
                        case ItemTouchHelper.UP /*1*/:
                        case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                            if (ItemTouchHelper.this.mVelocityTracker != null) {
                                ItemTouchHelper.this.mVelocityTracker.computeCurrentVelocity(LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, (float) ItemTouchHelper.this.mRecyclerView.getMaxFlingVelocity());
                            }
                            ItemTouchHelper.this.select(null, ItemTouchHelper.ACTION_STATE_IDLE);
                            ItemTouchHelper.this.mActivePointerId = ItemTouchHelper.ACTIVE_POINTER_ID_NONE;
                        case ItemTouchHelper.DOWN /*2*/:
                            if (activePointerIndex >= 0) {
                                ItemTouchHelper.this.updateDxDy(event, ItemTouchHelper.this.mSelectedFlags, activePointerIndex);
                                ItemTouchHelper.this.moveIfNecessary(viewHolder);
                                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                                ItemTouchHelper.this.mScrollRunnable.run();
                                ItemTouchHelper.this.mRecyclerView.invalidate();
                            }
                        case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                            int pointerIndex = MotionEventCompat.getActionIndex(event);
                            if (MotionEventCompat.getPointerId(event, pointerIndex) == ItemTouchHelper.this.mActivePointerId) {
                                if (ItemTouchHelper.this.mVelocityTracker != null) {
                                    ItemTouchHelper.this.mVelocityTracker.computeCurrentVelocity(LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, (float) ItemTouchHelper.this.mRecyclerView.getMaxFlingVelocity());
                                }
                                if (pointerIndex == 0) {
                                    newPointerIndex = ItemTouchHelper.UP;
                                }
                                ItemTouchHelper.this.mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                                ItemTouchHelper.this.updateDxDy(event, ItemTouchHelper.this.mSelectedFlags, pointerIndex);
                            }
                        default:
                    }
                }
            }
        }

        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (disallowIntercept) {
                ItemTouchHelper.this.select(null, ItemTouchHelper.ACTION_STATE_IDLE);
            }
        }
    }

    private class RecoverAnimation implements AnimatorListenerCompat {
        final int mActionState;
        private final int mAnimationType;
        private boolean mEnded;
        private float mFraction;
        public boolean mIsPendingCleanup;
        boolean mOverridden;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        private final ValueAnimatorCompat mValueAnimator;
        final ViewHolder mViewHolder;
        float mX;
        float mY;

        /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.RecoverAnimation.1 */
        class C01611 implements AnimatorUpdateListenerCompat {
            final /* synthetic */ ItemTouchHelper val$this$0;

            C01611(ItemTouchHelper itemTouchHelper) {
                this.val$this$0 = itemTouchHelper;
            }

            public void onAnimationUpdate(ValueAnimatorCompat animation) {
                RecoverAnimation.this.setFraction(animation.getAnimatedFraction());
            }
        }

        public RecoverAnimation(ViewHolder viewHolder, int animationType, int actionState, float startDx, float startDy, float targetX, float targetY) {
            this.mOverridden = ItemTouchHelper.DEBUG;
            this.mEnded = ItemTouchHelper.DEBUG;
            this.mActionState = actionState;
            this.mAnimationType = animationType;
            this.mViewHolder = viewHolder;
            this.mStartDx = startDx;
            this.mStartDy = startDy;
            this.mTargetX = targetX;
            this.mTargetY = targetY;
            this.mValueAnimator = AnimatorCompatHelper.emptyValueAnimator();
            this.mValueAnimator.addUpdateListener(new C01611(ItemTouchHelper.this));
            this.mValueAnimator.setTarget(viewHolder.itemView);
            this.mValueAnimator.addListener(this);
            setFraction(0.0f);
        }

        public void setDuration(long duration) {
            this.mValueAnimator.setDuration(duration);
        }

        public void start() {
            this.mViewHolder.setIsRecyclable(ItemTouchHelper.DEBUG);
            this.mValueAnimator.start();
        }

        public void cancel() {
            this.mValueAnimator.cancel();
        }

        public void setFraction(float fraction) {
            this.mFraction = fraction;
        }

        public void update() {
            if (this.mStartDx == this.mTargetX) {
                this.mX = ViewCompat.getTranslationX(this.mViewHolder.itemView);
            } else {
                this.mX = this.mStartDx + (this.mFraction * (this.mTargetX - this.mStartDx));
            }
            if (this.mStartDy == this.mTargetY) {
                this.mY = ViewCompat.getTranslationY(this.mViewHolder.itemView);
            } else {
                this.mY = this.mStartDy + (this.mFraction * (this.mTargetY - this.mStartDy));
            }
        }

        public void onAnimationStart(ValueAnimatorCompat animation) {
        }

        public void onAnimationEnd(ValueAnimatorCompat animation) {
            if (!this.mEnded) {
                this.mViewHolder.setIsRecyclable(true);
            }
            this.mEnded = true;
        }

        public void onAnimationCancel(ValueAnimatorCompat animation) {
            setFraction(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }

        public void onAnimationRepeat(ValueAnimatorCompat animation) {
        }
    }

    /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.3 */
    class C01563 extends RecoverAnimation {
        final /* synthetic */ ViewHolder val$prevSelected;
        final /* synthetic */ int val$swipeDir;

        C01563(ViewHolder x0, int x1, int x2, float x3, float x4, float x5, float x6, int i, ViewHolder viewHolder) {
            this.val$swipeDir = i;
            this.val$prevSelected = viewHolder;
            super(x0, x1, x2, x3, x4, x5, x6);
        }

        public void onAnimationEnd(ValueAnimatorCompat animation) {
            super.onAnimationEnd(animation);
            if (!this.mOverridden) {
                if (this.val$swipeDir <= 0) {
                    ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, this.val$prevSelected);
                } else {
                    ItemTouchHelper.this.mPendingCleanup.add(this.val$prevSelected.itemView);
                    this.mIsPendingCleanup = true;
                    if (this.val$swipeDir > 0) {
                        ItemTouchHelper.this.postDispatchSwipe(this, this.val$swipeDir);
                    }
                }
                if (ItemTouchHelper.this.mOverdrawChild == this.val$prevSelected.itemView) {
                    ItemTouchHelper.this.removeChildDrawingOrderCallbackIfNecessary(this.val$prevSelected.itemView);
                }
            }
        }
    }

    /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.4 */
    class C01574 implements Runnable {
        final /* synthetic */ RecoverAnimation val$anim;
        final /* synthetic */ int val$swipeDir;

        C01574(RecoverAnimation recoverAnimation, int i) {
            this.val$anim = recoverAnimation;
            this.val$swipeDir = i;
        }

        public void run() {
            if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !this.val$anim.mOverridden && this.val$anim.mViewHolder.getAdapterPosition() != ItemTouchHelper.ACTIVE_POINTER_ID_NONE) {
                ItemAnimator animator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
                if ((animator == null || !animator.isRunning(null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                    ItemTouchHelper.this.mCallback.onSwiped(this.val$anim.mViewHolder, this.val$swipeDir);
                } else {
                    ItemTouchHelper.this.mRecyclerView.post(this);
                }
            }
        }
    }

    /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.5 */
    class C01585 implements ChildDrawingOrderCallback {
        C01585() {
        }

        public int onGetChildDrawingOrder(int childCount, int i) {
            if (ItemTouchHelper.this.mOverdrawChild == null) {
                return i;
            }
            int childPosition = ItemTouchHelper.this.mOverdrawChildPosition;
            if (childPosition == ItemTouchHelper.ACTIVE_POINTER_ID_NONE) {
                childPosition = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                ItemTouchHelper.this.mOverdrawChildPosition = childPosition;
            }
            if (i == childCount + ItemTouchHelper.ACTIVE_POINTER_ID_NONE) {
                return childPosition;
            }
            return i >= childPosition ? i + ItemTouchHelper.UP : i;
        }
    }

    public static abstract class Callback {
        private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000;
        static final int RELATIVE_DIR_FLAGS = 3158064;
        private static final Interpolator sDragScrollInterpolator;
        private static final Interpolator sDragViewScrollCapInterpolator;
        private static final ItemTouchUIUtil sUICallback;
        private int mCachedMaxScrollSpeed;

        /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.Callback.1 */
        static class C01591 implements Interpolator {
            C01591() {
            }

            public float getInterpolation(float t) {
                return (((t * t) * t) * t) * t;
            }
        }

        /* renamed from: android.support.v7.widget.helper.ItemTouchHelper.Callback.2 */
        static class C01602 implements Interpolator {
            C01602() {
            }

            public float getInterpolation(float t) {
                t -= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
                return ((((t * t) * t) * t) * t) + DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            }
        }

        public abstract int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder);

        public abstract boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder viewHolder2);

        public abstract void onSwiped(ViewHolder viewHolder, int i);

        public Callback() {
            this.mCachedMaxScrollSpeed = ItemTouchHelper.ACTIVE_POINTER_ID_NONE;
        }

        static {
            sDragScrollInterpolator = new C01591();
            sDragViewScrollCapInterpolator = new C01602();
            if (VERSION.SDK_INT >= 21) {
                sUICallback = new Lollipop();
            } else if (VERSION.SDK_INT >= 11) {
                sUICallback = new Honeycomb();
            } else {
                sUICallback = new Gingerbread();
            }
        }

        public static ItemTouchUIUtil getDefaultUIUtil() {
            return sUICallback;
        }

        public static int convertToRelativeDirection(int flags, int layoutDirection) {
            int masked = flags & ABS_HORIZONTAL_DIR_FLAGS;
            if (masked == 0) {
                return flags;
            }
            flags &= masked ^ ItemTouchHelper.ACTIVE_POINTER_ID_NONE;
            if (layoutDirection == 0) {
                return flags | (masked << ItemTouchHelper.DOWN);
            }
            return (flags | ((masked << ItemTouchHelper.UP) & -789517)) | (((masked << ItemTouchHelper.UP) & ABS_HORIZONTAL_DIR_FLAGS) << ItemTouchHelper.DOWN);
        }

        public static int makeMovementFlags(int dragFlags, int swipeFlags) {
            return (makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, swipeFlags | dragFlags) | makeFlag(ItemTouchHelper.UP, swipeFlags)) | makeFlag(ItemTouchHelper.DOWN, dragFlags);
        }

        public static int makeFlag(int actionState, int directions) {
            return directions << (actionState * ItemTouchHelper.RIGHT);
        }

        public int convertToAbsoluteDirection(int flags, int layoutDirection) {
            int masked = flags & RELATIVE_DIR_FLAGS;
            if (masked == 0) {
                return flags;
            }
            flags &= masked ^ ItemTouchHelper.ACTIVE_POINTER_ID_NONE;
            if (layoutDirection == 0) {
                return flags | (masked >> ItemTouchHelper.DOWN);
            }
            return (flags | ((masked >> ItemTouchHelper.UP) & -3158065)) | (((masked >> ItemTouchHelper.UP) & RELATIVE_DIR_FLAGS) >> ItemTouchHelper.DOWN);
        }

        final int getAbsoluteMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            return convertToAbsoluteDirection(getMovementFlags(recyclerView, viewHolder), ViewCompat.getLayoutDirection(recyclerView));
        }

        private boolean hasDragFlag(RecyclerView recyclerView, ViewHolder viewHolder) {
            return (ItemTouchHelper.ACTION_MODE_DRAG_MASK & getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0 ? true : ItemTouchHelper.DEBUG;
        }

        private boolean hasSwipeFlag(RecyclerView recyclerView, ViewHolder viewHolder) {
            return (ItemTouchHelper.ACTION_MODE_SWIPE_MASK & getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0 ? true : ItemTouchHelper.DEBUG;
        }

        public boolean canDropOver(RecyclerView recyclerView, ViewHolder current, ViewHolder target) {
            return true;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public int getBoundingBoxMargin() {
            return ItemTouchHelper.ACTION_STATE_IDLE;
        }

        public float getSwipeThreshold(ViewHolder viewHolder) {
            return 0.5f;
        }

        public float getMoveThreshold(ViewHolder viewHolder) {
            return 0.5f;
        }

        public ViewHolder chooseDropTarget(ViewHolder selected, List<ViewHolder> dropTargets, int curX, int curY) {
            int right = curX + selected.itemView.getWidth();
            int bottom = curY + selected.itemView.getHeight();
            ViewHolder winner = null;
            int winnerScore = ItemTouchHelper.ACTIVE_POINTER_ID_NONE;
            int dx = curX - selected.itemView.getLeft();
            int dy = curY - selected.itemView.getTop();
            int targetsSize = dropTargets.size();
            for (int i = ItemTouchHelper.ACTION_STATE_IDLE; i < targetsSize; i += ItemTouchHelper.UP) {
                int diff;
                int score;
                ViewHolder target = (ViewHolder) dropTargets.get(i);
                if (dx > 0) {
                    diff = target.itemView.getRight() - right;
                    if (diff < 0 && target.itemView.getRight() > selected.itemView.getRight()) {
                        score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }
                if (dx < 0) {
                    diff = target.itemView.getLeft() - curX;
                    if (diff > 0 && target.itemView.getLeft() < selected.itemView.getLeft()) {
                        score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }
                if (dy < 0) {
                    diff = target.itemView.getTop() - curY;
                    if (diff > 0 && target.itemView.getTop() < selected.itemView.getTop()) {
                        score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }
                if (dy > 0) {
                    diff = target.itemView.getBottom() - bottom;
                    if (diff < 0 && target.itemView.getBottom() > selected.itemView.getBottom()) {
                        score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }
            }
            return winner;
        }

        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                sUICallback.onSelected(viewHolder.itemView);
            }
        }

        private int getMaxDragScroll(RecyclerView recyclerView) {
            if (this.mCachedMaxScrollSpeed == ItemTouchHelper.ACTIVE_POINTER_ID_NONE) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(C0123R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return this.mCachedMaxScrollSpeed;
        }

        public void onMoved(RecyclerView recyclerView, ViewHolder viewHolder, int fromPos, ViewHolder target, int toPos, int x, int y) {
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof ViewDropHandler) {
                ((ViewDropHandler) layoutManager).prepareForDrop(viewHolder.itemView, target.itemView, x, y);
                return;
            }
            if (layoutManager.canScrollHorizontally()) {
                if (layoutManager.getDecoratedLeft(target.itemView) <= recyclerView.getPaddingLeft()) {
                    recyclerView.scrollToPosition(toPos);
                }
                if (layoutManager.getDecoratedRight(target.itemView) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                    recyclerView.scrollToPosition(toPos);
                }
            }
            if (layoutManager.canScrollVertically()) {
                if (layoutManager.getDecoratedTop(target.itemView) <= recyclerView.getPaddingTop()) {
                    recyclerView.scrollToPosition(toPos);
                }
                if (layoutManager.getDecoratedBottom(target.itemView) >= recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                    recyclerView.scrollToPosition(toPos);
                }
            }
        }

        private void onDraw(Canvas c, RecyclerView parent, ViewHolder selected, List<RecoverAnimation> recoverAnimationList, int actionState, float dX, float dY) {
            int recoverAnimSize = recoverAnimationList.size();
            for (int i = ItemTouchHelper.ACTION_STATE_IDLE; i < recoverAnimSize; i += ItemTouchHelper.UP) {
                RecoverAnimation anim = (RecoverAnimation) recoverAnimationList.get(i);
                anim.update();
                int count = c.save();
                onChildDraw(c, parent, anim.mViewHolder, anim.mX, anim.mY, anim.mActionState, ItemTouchHelper.DEBUG);
                c.restoreToCount(count);
            }
            if (selected != null) {
                count = c.save();
                onChildDraw(c, parent, selected, dX, dY, actionState, true);
                c.restoreToCount(count);
            }
        }

        private void onDrawOver(Canvas c, RecyclerView parent, ViewHolder selected, List<RecoverAnimation> recoverAnimationList, int actionState, float dX, float dY) {
            int i;
            int recoverAnimSize = recoverAnimationList.size();
            for (i = ItemTouchHelper.ACTION_STATE_IDLE; i < recoverAnimSize; i += ItemTouchHelper.UP) {
                RecoverAnimation anim = (RecoverAnimation) recoverAnimationList.get(i);
                int count = c.save();
                onChildDrawOver(c, parent, anim.mViewHolder, anim.mX, anim.mY, anim.mActionState, ItemTouchHelper.DEBUG);
                c.restoreToCount(count);
            }
            if (selected != null) {
                count = c.save();
                onChildDrawOver(c, parent, selected, dX, dY, actionState, true);
                c.restoreToCount(count);
            }
            boolean hasRunningAnimation = ItemTouchHelper.DEBUG;
            for (i = recoverAnimSize + ItemTouchHelper.ACTIVE_POINTER_ID_NONE; i >= 0; i += ItemTouchHelper.ACTIVE_POINTER_ID_NONE) {
                anim = (RecoverAnimation) recoverAnimationList.get(i);
                if (anim.mEnded && !anim.mIsPendingCleanup) {
                    recoverAnimationList.remove(i);
                } else if (!anim.mEnded) {
                    hasRunningAnimation = true;
                }
            }
            if (hasRunningAnimation) {
                parent.invalidate();
            }
        }

        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            sUICallback.clearView(viewHolder.itemView);
        }

        public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            sUICallback.onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
        }

        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            sUICallback.onDrawOver(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
        }

        public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
            ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            return itemAnimator == null ? animationType == ItemTouchHelper.RIGHT ? 200 : 250 : animationType == ItemTouchHelper.RIGHT ? itemAnimator.getMoveDuration() : itemAnimator.getRemoveDuration();
        }

        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
            float timeRatio;
            int cappedScroll = (int) (((float) (((int) Math.signum((float) viewSizeOutOfBounds)) * getMaxDragScroll(recyclerView))) * sDragViewScrollCapInterpolator.getInterpolation(Math.min(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, (DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) Math.abs(viewSizeOutOfBounds))) / ((float) viewSize))));
            if (msSinceStartScroll > DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS) {
                timeRatio = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            } else {
                timeRatio = ((float) msSinceStartScroll) / 2000.0f;
            }
            int value = (int) (((float) cappedScroll) * sDragScrollInterpolator.getInterpolation(timeRatio));
            if (value == 0) {
                return viewSizeOutOfBounds > 0 ? ItemTouchHelper.UP : ItemTouchHelper.ACTIVE_POINTER_ID_NONE;
            } else {
                return value;
            }
        }
    }

    private class ItemTouchHelperGestureListener extends SimpleOnGestureListener {
        private ItemTouchHelperGestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            return true;
        }

        public void onLongPress(MotionEvent e) {
            View child = ItemTouchHelper.this.findChildView(e);
            if (child != null) {
                ViewHolder vh = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(child);
                if (vh != null && ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, vh) && MotionEventCompat.getPointerId(e, ItemTouchHelper.ACTION_STATE_IDLE) == ItemTouchHelper.this.mActivePointerId) {
                    int index = MotionEventCompat.findPointerIndex(e, ItemTouchHelper.this.mActivePointerId);
                    float x = MotionEventCompat.getX(e, index);
                    float y = MotionEventCompat.getY(e, index);
                    ItemTouchHelper.this.mInitialTouchX = x;
                    ItemTouchHelper.this.mInitialTouchY = y;
                    ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                    ItemTouchHelper.this.mDy = 0.0f;
                    itemTouchHelper.mDx = 0.0f;
                    if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled()) {
                        ItemTouchHelper.this.select(vh, ItemTouchHelper.DOWN);
                    }
                }
            }
        }
    }

    public static abstract class SimpleCallback extends Callback {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;

        public SimpleCallback(int dragDirs, int swipeDirs) {
            this.mDefaultSwipeDirs = swipeDirs;
            this.mDefaultDragDirs = dragDirs;
        }

        public void setDefaultSwipeDirs(int defaultSwipeDirs) {
            this.mDefaultSwipeDirs = defaultSwipeDirs;
        }

        public void setDefaultDragDirs(int defaultDragDirs) {
            this.mDefaultDragDirs = defaultDragDirs;
        }

        public int getSwipeDirs(RecyclerView recyclerView, ViewHolder viewHolder) {
            return this.mDefaultSwipeDirs;
        }

        public int getDragDirs(RecyclerView recyclerView, ViewHolder viewHolder) {
            return this.mDefaultDragDirs;
        }

        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            return Callback.makeMovementFlags(getDragDirs(recyclerView, viewHolder), getSwipeDirs(recyclerView, viewHolder));
        }
    }

    public ItemTouchHelper(Callback callback) {
        this.mPendingCleanup = new ArrayList();
        this.mTmpPosition = new float[DOWN];
        this.mSelected = null;
        this.mActivePointerId = ACTIVE_POINTER_ID_NONE;
        this.mActionState = ACTION_STATE_IDLE;
        this.mRecoverAnimations = new ArrayList();
        this.mScrollRunnable = new C01541();
        this.mChildDrawingOrderCallback = null;
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = ACTIVE_POINTER_ID_NONE;
        this.mOnItemTouchListener = new C01552();
        this.mCallback = callback;
    }

    private static boolean hitTest(View child, float x, float y, float left, float top) {
        return (x < left || x > ((float) child.getWidth()) + left || y < top || y > ((float) child.getHeight()) + top) ? DEBUG : true;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (this.mRecyclerView != recyclerView) {
            if (this.mRecyclerView != null) {
                destroyCallbacks();
            }
            this.mRecyclerView = recyclerView;
            if (this.mRecyclerView != null) {
                setupCallbacks();
            }
        }
    }

    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this);
        initGestureDetector();
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
        for (int i = this.mRecoverAnimations.size() + ACTIVE_POINTER_ID_NONE; i >= 0; i += ACTIVE_POINTER_ID_NONE) {
            this.mCallback.clearView(this.mRecyclerView, ((RecoverAnimation) this.mRecoverAnimations.get(ACTION_STATE_IDLE)).mViewHolder);
        }
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = ACTIVE_POINTER_ID_NONE;
        releaseVelocityTracker();
    }

    private void initGestureDetector() {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), new ItemTouchHelperGestureListener());
        }
    }

    private void getSelectedDxDy(float[] outPosition) {
        if ((this.mSelectedFlags & 12) != 0) {
            outPosition[ACTION_STATE_IDLE] = (this.mSelectedStartX + this.mDx) - ((float) this.mSelected.itemView.getLeft());
        } else {
            outPosition[ACTION_STATE_IDLE] = ViewCompat.getTranslationX(this.mSelected.itemView);
        }
        if ((this.mSelectedFlags & 3) != 0) {
            outPosition[UP] = (this.mSelectedStartY + this.mDy) - ((float) this.mSelected.itemView.getTop());
        } else {
            outPosition[UP] = ViewCompat.getTranslationY(this.mSelected.itemView);
        }
    }

    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        float dx = 0.0f;
        float dy = 0.0f;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            dx = this.mTmpPosition[ACTION_STATE_IDLE];
            dy = this.mTmpPosition[UP];
        }
        this.mCallback.onDrawOver(c, parent, this.mSelected, this.mRecoverAnimations, this.mActionState, dx, dy);
    }

    public void onDraw(Canvas c, RecyclerView parent, State state) {
        this.mOverdrawChildPosition = ACTIVE_POINTER_ID_NONE;
        float dx = 0.0f;
        float dy = 0.0f;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            dx = this.mTmpPosition[ACTION_STATE_IDLE];
            dy = this.mTmpPosition[UP];
        }
        this.mCallback.onDraw(c, parent, this.mSelected, this.mRecoverAnimations, this.mActionState, dx, dy);
    }

    private void select(ViewHolder selected, int actionState) {
        if (selected != this.mSelected || actionState != this.mActionState) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            int prevActionState = this.mActionState;
            endRecoverAnimation(selected, true);
            this.mActionState = actionState;
            if (actionState == DOWN) {
                this.mOverdrawChild = selected.itemView;
                addChildDrawingOrderCallback();
            }
            int actionStateMask = (UP << ((actionState * RIGHT) + RIGHT)) + ACTIVE_POINTER_ID_NONE;
            boolean preventLayout = DEBUG;
            if (this.mSelected != null) {
                ViewHolder prevSelected = this.mSelected;
                if (prevSelected.itemView.getParent() != null) {
                    int swipeDir;
                    float targetTranslateX;
                    float targetTranslateY;
                    int animationType;
                    if (prevActionState == DOWN) {
                        swipeDir = ACTION_STATE_IDLE;
                    } else {
                        swipeDir = swipeIfNecessary(prevSelected);
                    }
                    releaseVelocityTracker();
                    switch (swipeDir) {
                        case UP /*1*/:
                        case DOWN /*2*/:
                            targetTranslateX = 0.0f;
                            targetTranslateY = Math.signum(this.mDy) * ((float) this.mRecyclerView.getHeight());
                            break;
                        case LEFT /*4*/:
                        case RIGHT /*8*/:
                        case START /*16*/:
                        case END /*32*/:
                            targetTranslateY = 0.0f;
                            targetTranslateX = Math.signum(this.mDx) * ((float) this.mRecyclerView.getWidth());
                            break;
                        default:
                            targetTranslateX = 0.0f;
                            targetTranslateY = 0.0f;
                            break;
                    }
                    if (prevActionState == DOWN) {
                        animationType = RIGHT;
                    } else if (swipeDir > 0) {
                        animationType = DOWN;
                    } else {
                        animationType = LEFT;
                    }
                    getSelectedDxDy(this.mTmpPosition);
                    float currentTranslateX = this.mTmpPosition[ACTION_STATE_IDLE];
                    float currentTranslateY = this.mTmpPosition[UP];
                    RecoverAnimation rv = new C01563(prevSelected, animationType, prevActionState, currentTranslateX, currentTranslateY, targetTranslateX, targetTranslateY, swipeDir, prevSelected);
                    rv.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, animationType, targetTranslateX - currentTranslateX, targetTranslateY - currentTranslateY));
                    this.mRecoverAnimations.add(rv);
                    rv.start();
                    preventLayout = true;
                } else {
                    removeChildDrawingOrderCallbackIfNecessary(prevSelected.itemView);
                    this.mCallback.clearView(this.mRecyclerView, prevSelected);
                }
                this.mSelected = null;
            }
            if (selected != null) {
                this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, selected) & actionStateMask) >> (this.mActionState * RIGHT);
                this.mSelectedStartX = (float) selected.itemView.getLeft();
                this.mSelectedStartY = (float) selected.itemView.getTop();
                this.mSelected = selected;
                if (actionState == DOWN) {
                    this.mSelected.itemView.performHapticFeedback(ACTION_STATE_IDLE);
                }
            }
            ViewParent rvParent = this.mRecyclerView.getParent();
            if (rvParent != null) {
                rvParent.requestDisallowInterceptTouchEvent(this.mSelected != null ? true : DEBUG);
            }
            if (!preventLayout) {
                this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
            }
            this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
            this.mRecyclerView.invalidate();
        }
    }

    private void postDispatchSwipe(RecoverAnimation anim, int swipeDir) {
        this.mRecyclerView.post(new C01574(anim, swipeDir));
    }

    private boolean hasRunningRecoverAnim() {
        int size = this.mRecoverAnimations.size();
        for (int i = ACTION_STATE_IDLE; i < size; i += UP) {
            if (!((RecoverAnimation) this.mRecoverAnimations.get(i)).mEnded) {
                return true;
            }
        }
        return DEBUG;
    }

    private boolean scrollIfNecessary() {
        if (this.mSelected == null) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return DEBUG;
        }
        long scrollDuration;
        long now = System.currentTimeMillis();
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            scrollDuration = 0;
        } else {
            scrollDuration = now - this.mDragScrollStartTimeInMs;
        }
        LayoutManager lm = this.mRecyclerView.getLayoutManager();
        if (this.mTmpRect == null) {
            this.mTmpRect = new Rect();
        }
        int scrollX = ACTION_STATE_IDLE;
        int scrollY = ACTION_STATE_IDLE;
        lm.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
        if (lm.canScrollHorizontally()) {
            int curX = (int) (this.mSelectedStartX + this.mDx);
            int leftDiff = (curX - this.mTmpRect.left) - this.mRecyclerView.getPaddingLeft();
            if (this.mDx < 0.0f && leftDiff < 0) {
                scrollX = leftDiff;
            } else if (this.mDx > 0.0f) {
                int rightDiff = ((this.mSelected.itemView.getWidth() + curX) + this.mTmpRect.right) - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight());
                if (rightDiff > 0) {
                    scrollX = rightDiff;
                }
            }
        }
        if (lm.canScrollVertically()) {
            int curY = (int) (this.mSelectedStartY + this.mDy);
            int topDiff = (curY - this.mTmpRect.top) - this.mRecyclerView.getPaddingTop();
            if (this.mDy < 0.0f && topDiff < 0) {
                scrollY = topDiff;
            } else if (this.mDy > 0.0f) {
                int bottomDiff = ((this.mSelected.itemView.getHeight() + curY) + this.mTmpRect.bottom) - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom());
                if (bottomDiff > 0) {
                    scrollY = bottomDiff;
                }
            }
        }
        if (scrollX != 0) {
            scrollX = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), scrollX, this.mRecyclerView.getWidth(), scrollDuration);
        }
        if (scrollY != 0) {
            scrollY = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), scrollY, this.mRecyclerView.getHeight(), scrollDuration);
        }
        if (scrollX == 0 && scrollY == 0) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return DEBUG;
        }
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            this.mDragScrollStartTimeInMs = now;
        }
        this.mRecyclerView.scrollBy(scrollX, scrollY);
        return true;
    }

    private List<ViewHolder> findSwapTargets(ViewHolder viewHolder) {
        if (this.mSwapTargets == null) {
            this.mSwapTargets = new ArrayList();
            this.mDistances = new ArrayList();
        } else {
            this.mSwapTargets.clear();
            this.mDistances.clear();
        }
        int margin = this.mCallback.getBoundingBoxMargin();
        int left = Math.round(this.mSelectedStartX + this.mDx) - margin;
        int top = Math.round(this.mSelectedStartY + this.mDy) - margin;
        int right = (viewHolder.itemView.getWidth() + left) + (margin * DOWN);
        int bottom = (viewHolder.itemView.getHeight() + top) + (margin * DOWN);
        int centerX = (left + right) / DOWN;
        int centerY = (top + bottom) / DOWN;
        LayoutManager lm = this.mRecyclerView.getLayoutManager();
        int childCount = lm.getChildCount();
        for (int i = ACTION_STATE_IDLE; i < childCount; i += UP) {
            View other = lm.getChildAt(i);
            if (other != viewHolder.itemView && other.getBottom() >= top && other.getTop() <= bottom && other.getRight() >= left && other.getLeft() <= right) {
                ViewHolder otherVh = this.mRecyclerView.getChildViewHolder(other);
                if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, otherVh)) {
                    int dx = Math.abs(centerX - ((other.getLeft() + other.getRight()) / DOWN));
                    int dy = Math.abs(centerY - ((other.getTop() + other.getBottom()) / DOWN));
                    int dist = (dx * dx) + (dy * dy);
                    int pos = ACTION_STATE_IDLE;
                    int cnt = this.mSwapTargets.size();
                    for (int j = ACTION_STATE_IDLE; j < cnt; j += UP) {
                        if (dist <= ((Integer) this.mDistances.get(j)).intValue()) {
                            break;
                        }
                        pos += UP;
                    }
                    this.mSwapTargets.add(pos, otherVh);
                    this.mDistances.add(pos, Integer.valueOf(dist));
                }
            }
        }
        return this.mSwapTargets;
    }

    private void moveIfNecessary(ViewHolder viewHolder) {
        if (!this.mRecyclerView.isLayoutRequested() && this.mActionState == DOWN) {
            float threshold = this.mCallback.getMoveThreshold(viewHolder);
            int x = (int) (this.mSelectedStartX + this.mDx);
            int y = (int) (this.mSelectedStartY + this.mDy);
            if (((float) Math.abs(y - viewHolder.itemView.getTop())) >= ((float) viewHolder.itemView.getHeight()) * threshold || ((float) Math.abs(x - viewHolder.itemView.getLeft())) >= ((float) viewHolder.itemView.getWidth()) * threshold) {
                List<ViewHolder> swapTargets = findSwapTargets(viewHolder);
                if (swapTargets.size() != 0) {
                    ViewHolder target = this.mCallback.chooseDropTarget(viewHolder, swapTargets, x, y);
                    if (target == null) {
                        this.mSwapTargets.clear();
                        this.mDistances.clear();
                        return;
                    }
                    int toPosition = target.getAdapterPosition();
                    int fromPosition = viewHolder.getAdapterPosition();
                    if (this.mCallback.onMove(this.mRecyclerView, viewHolder, target)) {
                        this.mCallback.onMoved(this.mRecyclerView, viewHolder, fromPosition, target, toPosition, x, y);
                    }
                }
            }
        }
    }

    public void onChildViewAttachedToWindow(View view) {
    }

    public void onChildViewDetachedFromWindow(View view) {
        removeChildDrawingOrderCallbackIfNecessary(view);
        ViewHolder holder = this.mRecyclerView.getChildViewHolder(view);
        if (holder != null) {
            if (this.mSelected == null || holder != this.mSelected) {
                endRecoverAnimation(holder, DEBUG);
                if (this.mPendingCleanup.remove(holder.itemView)) {
                    this.mCallback.clearView(this.mRecyclerView, holder);
                    return;
                }
                return;
            }
            select(null, ACTION_STATE_IDLE);
        }
    }

    private int endRecoverAnimation(ViewHolder viewHolder, boolean override) {
        for (int i = this.mRecoverAnimations.size() + ACTIVE_POINTER_ID_NONE; i >= 0; i += ACTIVE_POINTER_ID_NONE) {
            RecoverAnimation anim = (RecoverAnimation) this.mRecoverAnimations.get(i);
            if (anim.mViewHolder == viewHolder) {
                anim.mOverridden |= override;
                if (!anim.mEnded) {
                    anim.cancel();
                }
                this.mRecoverAnimations.remove(i);
                return anim.mAnimationType;
            }
        }
        return ACTION_STATE_IDLE;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        outRect.setEmpty();
    }

    private void obtainVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private ViewHolder findSwipedView(MotionEvent motionEvent) {
        LayoutManager lm = this.mRecyclerView.getLayoutManager();
        if (this.mActivePointerId == ACTIVE_POINTER_ID_NONE) {
            return null;
        }
        int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
        float dy = MotionEventCompat.getY(motionEvent, pointerIndex) - this.mInitialTouchY;
        float absDx = Math.abs(MotionEventCompat.getX(motionEvent, pointerIndex) - this.mInitialTouchX);
        float absDy = Math.abs(dy);
        if (absDx < ((float) this.mSlop) && absDy < ((float) this.mSlop)) {
            return null;
        }
        if (absDx > absDy && lm.canScrollHorizontally()) {
            return null;
        }
        if (absDy > absDx && lm.canScrollVertically()) {
            return null;
        }
        View child = findChildView(motionEvent);
        if (child != null) {
            return this.mRecyclerView.getChildViewHolder(child);
        }
        return null;
    }

    private boolean checkSelectForSwipe(int action, MotionEvent motionEvent, int pointerIndex) {
        if (this.mSelected != null || action != DOWN || this.mActionState == DOWN || !this.mCallback.isItemViewSwipeEnabled()) {
            return DEBUG;
        }
        if (this.mRecyclerView.getScrollState() == UP) {
            return DEBUG;
        }
        ViewHolder vh = findSwipedView(motionEvent);
        if (vh == null) {
            return DEBUG;
        }
        int swipeFlags = (ACTION_MODE_SWIPE_MASK & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, vh)) >> RIGHT;
        if (swipeFlags == 0) {
            return DEBUG;
        }
        float x = MotionEventCompat.getX(motionEvent, pointerIndex);
        float dx = x - this.mInitialTouchX;
        float dy = MotionEventCompat.getY(motionEvent, pointerIndex) - this.mInitialTouchY;
        float absDx = Math.abs(dx);
        float absDy = Math.abs(dy);
        if (absDx < ((float) this.mSlop) && absDy < ((float) this.mSlop)) {
            return DEBUG;
        }
        if (absDx > absDy) {
            if (dx < 0.0f && (swipeFlags & LEFT) == 0) {
                return DEBUG;
            }
            if (dx > 0.0f && (swipeFlags & RIGHT) == 0) {
                return DEBUG;
            }
        } else if (dy < 0.0f && (swipeFlags & UP) == 0) {
            return DEBUG;
        } else {
            if (dy > 0.0f && (swipeFlags & DOWN) == 0) {
                return DEBUG;
            }
        }
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, ACTION_STATE_IDLE);
        select(vh, UP);
        return true;
    }

    private View findChildView(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (this.mSelected != null) {
            View selectedView = this.mSelected.itemView;
            if (hitTest(selectedView, x, y, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
                return selectedView;
            }
        }
        for (int i = this.mRecoverAnimations.size() + ACTIVE_POINTER_ID_NONE; i >= 0; i += ACTIVE_POINTER_ID_NONE) {
            RecoverAnimation anim = (RecoverAnimation) this.mRecoverAnimations.get(i);
            View view = anim.mViewHolder.itemView;
            if (hitTest(view, x, y, anim.mX, anim.mY)) {
                return view;
            }
        }
        return this.mRecyclerView.findChildViewUnder(x, y);
    }

    public void startDrag(ViewHolder viewHolder) {
        if (!this.mCallback.hasDragFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start drag has been called but swiping is not enabled");
        } else if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
        } else {
            obtainVelocityTracker();
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            select(viewHolder, DOWN);
        }
    }

    public void startSwipe(ViewHolder viewHolder) {
        if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start swipe has been called but dragging is not enabled");
        } else if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
        } else {
            obtainVelocityTracker();
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            select(viewHolder, UP);
        }
    }

    private RecoverAnimation findAnimation(MotionEvent event) {
        if (this.mRecoverAnimations.isEmpty()) {
            return null;
        }
        View target = findChildView(event);
        for (int i = this.mRecoverAnimations.size() + ACTIVE_POINTER_ID_NONE; i >= 0; i += ACTIVE_POINTER_ID_NONE) {
            RecoverAnimation anim = (RecoverAnimation) this.mRecoverAnimations.get(i);
            if (anim.mViewHolder.itemView == target) {
                return anim;
            }
        }
        return null;
    }

    private void updateDxDy(MotionEvent ev, int directionFlags, int pointerIndex) {
        float x = MotionEventCompat.getX(ev, pointerIndex);
        float y = MotionEventCompat.getY(ev, pointerIndex);
        this.mDx = x - this.mInitialTouchX;
        this.mDy = y - this.mInitialTouchY;
        if ((directionFlags & LEFT) == 0) {
            this.mDx = Math.max(0.0f, this.mDx);
        }
        if ((directionFlags & RIGHT) == 0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((directionFlags & UP) == 0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((directionFlags & DOWN) == 0) {
            this.mDy = Math.min(0.0f, this.mDy);
        }
    }

    private int swipeIfNecessary(ViewHolder viewHolder) {
        if (this.mActionState == DOWN) {
            return ACTION_STATE_IDLE;
        }
        int originalMovementFlags = this.mCallback.getMovementFlags(this.mRecyclerView, viewHolder);
        int flags = (this.mCallback.convertToAbsoluteDirection(originalMovementFlags, ViewCompat.getLayoutDirection(this.mRecyclerView)) & ACTION_MODE_SWIPE_MASK) >> RIGHT;
        if (flags == 0) {
            return ACTION_STATE_IDLE;
        }
        int originalFlags = (originalMovementFlags & ACTION_MODE_SWIPE_MASK) >> RIGHT;
        int swipeDir;
        if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
            swipeDir = checkHorizontalSwipe(viewHolder, flags);
            if (swipeDir <= 0) {
                swipeDir = checkVerticalSwipe(viewHolder, flags);
                if (swipeDir > 0) {
                    return swipeDir;
                }
            } else if ((originalFlags & swipeDir) == 0) {
                return Callback.convertToRelativeDirection(swipeDir, ViewCompat.getLayoutDirection(this.mRecyclerView));
            } else {
                return swipeDir;
            }
        }
        swipeDir = checkVerticalSwipe(viewHolder, flags);
        if (swipeDir > 0) {
            return swipeDir;
        }
        swipeDir = checkHorizontalSwipe(viewHolder, flags);
        if (swipeDir > 0) {
            if ((originalFlags & swipeDir) == 0) {
                return Callback.convertToRelativeDirection(swipeDir, ViewCompat.getLayoutDirection(this.mRecyclerView));
            }
            return swipeDir;
        }
        return ACTION_STATE_IDLE;
    }

    private int checkHorizontalSwipe(ViewHolder viewHolder, int flags) {
        if ((flags & 12) != 0) {
            int dirFlag = this.mDx > 0.0f ? RIGHT : LEFT;
            if (this.mVelocityTracker != null && this.mActivePointerId > ACTIVE_POINTER_ID_NONE) {
                int velDirFlag;
                float xVelocity = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId);
                if (xVelocity > 0.0f) {
                    velDirFlag = RIGHT;
                } else {
                    velDirFlag = LEFT;
                }
                if ((velDirFlag & flags) != 0 && dirFlag == velDirFlag && Math.abs(xVelocity) >= ((float) this.mRecyclerView.getMinFlingVelocity())) {
                    return velDirFlag;
                }
            }
            float threshold = ((float) this.mRecyclerView.getWidth()) * this.mCallback.getSwipeThreshold(viewHolder);
            if ((flags & dirFlag) != 0 && Math.abs(this.mDx) > threshold) {
                return dirFlag;
            }
        }
        return ACTION_STATE_IDLE;
    }

    private int checkVerticalSwipe(ViewHolder viewHolder, int flags) {
        if ((flags & 3) != 0) {
            int dirFlag = this.mDy > 0.0f ? DOWN : UP;
            if (this.mVelocityTracker != null && this.mActivePointerId > ACTIVE_POINTER_ID_NONE) {
                int velDirFlag;
                float yVelocity = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId);
                if (yVelocity > 0.0f) {
                    velDirFlag = DOWN;
                } else {
                    velDirFlag = UP;
                }
                if ((velDirFlag & flags) != 0 && velDirFlag == dirFlag && Math.abs(yVelocity) >= ((float) this.mRecyclerView.getMinFlingVelocity())) {
                    return velDirFlag;
                }
            }
            float threshold = ((float) this.mRecyclerView.getHeight()) * this.mCallback.getSwipeThreshold(viewHolder);
            if ((flags & dirFlag) != 0 && Math.abs(this.mDy) > threshold) {
                return dirFlag;
            }
        }
        return ACTION_STATE_IDLE;
    }

    private void addChildDrawingOrderCallback() {
        if (VERSION.SDK_INT < 21) {
            if (this.mChildDrawingOrderCallback == null) {
                this.mChildDrawingOrderCallback = new C01585();
            }
            this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
        }
    }

    private void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
            if (this.mChildDrawingOrderCallback != null) {
                this.mRecyclerView.setChildDrawingOrderCallback(null);
            }
        }
    }
}
