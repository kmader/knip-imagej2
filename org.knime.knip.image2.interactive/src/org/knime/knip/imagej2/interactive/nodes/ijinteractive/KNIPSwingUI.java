package org.knime.knip.imagej2.interactive.nodes.ijinteractive;

import imagej.display.Display;
import imagej.ui.DialogPrompt;
import imagej.ui.DialogPrompt.MessageType;
import imagej.ui.DialogPrompt.OptionType;
import imagej.ui.UserInterface;
import imagej.ui.common.awt.AWTDropTargetEventDispatcher;
import imagej.ui.common.awt.AWTInputEventDispatcher;
import imagej.ui.swing.sdi.SwingDialogPrompt;
import imagej.ui.viewer.DisplayWindow;

import org.scijava.plugin.Plugin;

@Plugin(type = UserInterface.class, name = KNIPSwingUI.NAME)
public class KNIPSwingUI extends KNIPAbstractSwingUI {

    public static final String NAME = "KNIP Swing UI";

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayWindow createDisplayWindow(final Display<?> display) {

        KNIPDisplayWindow knipDisplayWindow = new KNIPDisplayWindow(display.getName());

        // broadcast input events (keyboard and mouse)
        new AWTInputEventDispatcher(display).register(knipDisplayWindow, true, false);

        // broadcast drag-and-drop events
        new AWTDropTargetEventDispatcher(display, eventService);

        // add knipDisplay to our ui
        //TODO: let them move around
        appFrame.add(knipDisplayWindow);

        // return knipDisplay
        return knipDisplayWindow;
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
        appFrame = new KNIPApplicationFrame();
        appFrame.setVisible(true);
    }

    @Override
    public void show() {
        createUI();
    }
}
