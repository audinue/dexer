
import java.io.File;
import java.io.PrintWriter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import com.android.dx.command.Main;

class Dexer { 

	public static void main(String[] args) throws Throwable {
		FileAlterationMonitor monitor = new FileAlterationMonitor(3000);
		FileAlterationObserver observer = new FileAlterationObserver(new File("."));
		observer.addListener(new FileAlterationListenerAdaptor() {
				public void onFileChange(File file) {
					if (!file.getName().endsWith(".java")) {
						return;  
					}
					System.out.println("Compiling " + file);
					if (BatchCompiler.compile("-7 -cp commons-io-2.6.jar:ecj-4.6.1.jar:com.android.dx-1.12.jar:out -d out " + file, new PrintWriter(System.out), new PrintWriter(System.err), null)) {
						try {
							Main.main(new String[]{"--dex", "--output", "classes.dex", "out"});
						} catch (Throwable e) {
						}
					}
					System.out.println("Done!");
				}
		});
		monitor.addObserver(observer);
		monitor.start();
	}
}
