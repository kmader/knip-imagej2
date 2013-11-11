package org.knime.knip.imagej2.interactive.nodes.ijinteractive;

import imagej.display.Display;
import imagej.ui.DialogPrompt;
import imagej.ui.DialogPrompt.MessageType;
import imagej.ui.DialogPrompt.OptionType;
import imagej.ui.UserInterface;
import imagej.ui.swing.sdi.SwingDialogPrompt;
import imagej.ui.viewer.DisplayWindow;

import javax.swing.JPanel;

import org.scijava.plugin.Plugin;

@Plugin(type = UserInterface.class, name = KNIPSwingUI.NAME)
public class KNIPSwingUI extends KNIPAbstractSwingUI {

    public static final String NAME = "KNIP Swing UI";

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayWindow createDisplayWindow(final Display<?> display) {
        return new KNIPDisplayWindow(display);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DialogPrompt dialogPrompt(final String message, final String title, final MessageType messageType,
                                     final OptionType optionType) {
        return new SwingDialogPrompt(message, title, messageType, optionType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setupAppFrame() {
        appFrame = new KNIPApplicationFrame("IJ2 iN KNIP (volle cool)");
        final JPanel pane = new JPanel();
        appFrame.setContentPane(pane);
        //  TODO: here init layout
        appFrame.setVisible(true);
    }

    @Override
    public void show() {
        createUI();
        //        displayReadme();
    }
}
