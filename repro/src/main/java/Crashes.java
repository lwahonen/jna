import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public class Crashes {

    private static GuidCallback.TestCallback tester = new GuidCallback.TestCallback() {
        @Override
        public int callback(Guid.GUID test) {
            System.out.println("Received guid " + test);
            return 0;
        }
    };

    public static void main(String[] args) {
        GuidCallback.INSTANCE.testGuid(tester);
    }

    interface GuidCallback extends Library {

        GuidCallback INSTANCE = Native.load("callback.dll", GuidCallback.class, W32APIOptions.UNICODE_OPTIONS);

        void testGuid(TestCallback cb);

        interface TestCallback extends StdCallLibrary.StdCallCallback {
            int callback(Guid.GUID test);
        }
    }
}
