package org.knime.knip.imagej2.interactive.nodes.ijinteractive;

import imagej.data.Dataset;
import imagej.ui.UserInterface;
import imagej.ui.viewer.DisplayViewer;
import imagej.ui.viewer.image.AbstractImageDisplayViewer;

import org.scijava.plugin.Plugin;

@Plugin(type = DisplayViewer.class, name = "KNIP Display Viewer")
public class KNIPDisplayViewer extends AbstractImageDisplayViewer {

    /**
     * {@inheritDoc}
     */
    @Override
    public Dataset capture() {
        return null;
    }

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
        return -1;
    }

}
