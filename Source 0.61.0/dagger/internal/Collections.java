package dagger.internal;

import com.mopub.volley.DefaultRetryPolicy;
import com.upsight.mediation.vast.activity.PlayerControls;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

final class Collections {
    private static final int MAX_POWER_OF_TWO = 1073741824;

    private Collections() {
    }

    static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return new LinkedHashSet(calculateInitialCapacity(expectedSize));
    }

    static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
        return new LinkedHashMap(calculateInitialCapacity(expectedSize));
    }

    private static int calculateInitialCapacity(int expectedSize) {
        if (expectedSize < 3) {
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            return (int) ((((float) expectedSize) / PlayerControls.DOWN_STATE) + DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }
        return Integer.MAX_VALUE;
    }
}
