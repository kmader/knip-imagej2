package org.knime.knip.imagej2.interactive.nodes.ijinteractive;

import imagej.ui.UserInterface;
import imagej.ui.swing.viewer.image.AbstractSwingImageDisplayViewer;
import imagej.ui.viewer.DisplayViewer;

import org.scijava.plugin.Plugin;

@Plugin(type = DisplayViewer.class, name = "test")
public class KNIPDisplayViewer extends AbstractSwingImageDisplayViewer {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCompatible(final UserInterface ui) {
        return ui instanceof KNIPSwingUI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPriority() {
        return 2;
    }


}
