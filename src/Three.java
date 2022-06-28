import java.util.Arrays;

public class Three {
    private static class Version {
        private final String string;
        private final int major;
        private final int minor;
        private final int patch;

        private Version(final String string) {
            String[] tokens = string.split("\\.");
            int major = -1;
            int minor = -1;
            int patch = -1;

            if (tokens.length >= 3) {
                patch = Integer.parseInt(tokens[2]);
            }
            if (tokens.length >= 2) {
                minor = Integer.parseInt(tokens[1]);
            }
            if (tokens.length >= 1) {
                major = Integer.parseInt(tokens[0]);
            }

            this.string = string;
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }
    }

    private static int compare(int a, int b) {
        if (a == -1) return -1;
        if (b == -1) return 1;
        if (a < b) return -1;
        if (a > b) return 1;
        return 0;
    }

    public static String[] solution(String[] l) {
        final int n = l.length;
        Version[] versions = new Version[n];
        String[] result = new String[n];

        for (int i = 0; i < n; i++) {
            versions[i] = new Version(l[i]);
        }

        Arrays.sort(versions, (a, b) -> {
            if (a.major != b.major) {
                return compare(a.major, b.major);
            }
            if (a.minor != b.minor) {
                return compare(a.minor, b.minor);
            }
            if (a.patch != b.patch) {
                return compare(a.patch, b.patch);
            }
            return 0;
        });

        for (int i = 0; i < n; i++) {
            result[i] = versions[i].string;
        }

        return result;
    }

    public static void main(String[] args) {
        String[] solution = Three.solution(new String[]{"1.11", "2.0.0", "1.2", "2", "0.1", "1.2.1", "1.1.1", "2.0"});
        for (String s : solution) {
            System.out.print(s + ", ");
        }
    }
}
