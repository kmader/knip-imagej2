import net.imglib2.img.Img;
import net.imglib2.ops.img.UnaryOperationAssignment;
import net.imglib2.ops.operation.real.unary.RealAddConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/* Notes from SezPoz.java.net
 *
 * Notes
 *
 * Known limitations:
 *
 * When using JDK 5 and apt, incremental compilation can result in an index file being generated with only some of the desired entries,
 * if other source files are omitted e.g. by Ant. This scenario works better using JDK 6's javac: if you compile just some sources which
 * are marked with an indexable annotation, these entries will be appended to any existing registrations from previous runs of the compiler.
 * (You should run a clean build if you delete annotations from sources.)
 *
 * The Java language spec currently prohibits recursive annotation definitions, although javac in JDK 5 does not. (JDK 6 and 7's javac do.)
 * See bug #6264216.
 *
 * Eclipse-specific notes: make sure annotation processing is enabled at least for any projects registering objects using annotations.
 * Make sure the SezPoz library is in the factory path for annotation processors. You also need to check the box Run this container's processor
 * in batch mode from the Advanced button in Java Compiler > Annotation Processing > Factory Path. There does not appear to be any way for Eclipse
 * to discover processors in the regular classpath as JSR 269 suggests, and there does not appear to be any way to make these settings apply
 * automatically to all projects. Eclipse users are recommended to use javac (e.g. via Maven) to build. Eclipse Help Page Eclipse bug #280542
 *
 */

/*
 * Notes for fragment plugins
 * 
 * just wrap the plugin in a plugin fragment of imagej.core
 */

@Plugin(menu = { @Menu(label = "DeveloperPlugins"),
		@Menu(label = "Img Adapter Test") }, description = "can be used to test the img adapter (a KNIME only adapter)", headless = true, type = Command.class)
public class ImgDemoPlugin<T extends RealType<T>> implements Command {

	@Parameter(type = ItemIO.BOTH)
	private Img picture;

	@Parameter(type = ItemIO.OUTPUT)
	private String metaDescription;

	@Override
	public void run() {
		new UnaryOperationAssignment<T, T>(new RealAddConstant<T, T>(50))
				.compute(picture, picture);
		metaDescription = new String(picture.toString()
				+ " with first element: " + picture.firstElement().toString());
	}

}