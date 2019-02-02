package units;

public enum UV {
    LOW("You can safely stay outside",                    new int[]{0,1,2},  new int[]{}),
    MODERATE("Take care during midday hours",             new int[]{3,4,5},  new int[]{}),
    HIGH("Seek shade during midday hours",                new int[]{6,7},    new int[]{11,15}),
    VERY_HIGH("Spend time in the shade between 11 and 3", new int[]{8,9,10}, new int[]{11,15}),
    EXTREME("Avoid being outside during midday hours",    new int[]{11},     new int[]{11,15});

    final String message;
    private final int[] uvIndexes;
    private final int[] avoidBeingOutsideBetween;

    UV(String message, int[] uvIndexes, int[] avoidBeingOutsideBetween) {
        this.message = message;
        this.uvIndexes = uvIndexes;
        this.avoidBeingOutsideBetween = avoidBeingOutsideBetween;
    }

//    public static UV getUvFromIndex(int index) {
//
//    }
}
