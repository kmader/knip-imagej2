package org.knime.knip.imagej2.interactive.nodes.ijinteractive;

import imagej.display.Display;
import imagej.menu.DefaultMenuService;
import imagej.menu.MenuService;
import imagej.menu.ShadowMenu;
import imagej.platform.AppEventService;
import imagej.platform.event.AppMenusCreatedEvent;
import imagej.ui.ApplicationFrame;
import imagej.ui.Desktop;
import imagej.ui.DialogPrompt;
import imagej.ui.DialogPrompt.MessageType;
import imagej.ui.DialogPrompt.OptionType;
import imagej.ui.SystemClipboard;
import imagej.ui.UIService;
import imagej.ui.UserInterface;
import imagej.ui.common.awt.AWTClipboard;
import imagej.ui.common.awt.AWTDropTargetEventDispatcher;
import imagej.ui.common.awt.AWTInputEventDispatcher;
import imagej.ui.swing.SwingStatusBar;
import imagej.ui.swing.SwingToolBar;
import imagej.ui.swing.menu.SwingJMenuBarCreator;
import imagej.ui.swing.menu.SwingJPopupMenuCreator;
import imagej.ui.swing.sdi.SwingDialogPrompt;
import imagej.ui.swing.viewer.image.SwingDisplayPanel;
import imagej.ui.viewer.DisplayPanel;
import imagej.ui.viewer.DisplayViewer;
import imagej.ui.viewer.DisplayWindow;

import java.awt.Component;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.scijava.AbstractContextual;
import org.scijava.Context;
import org.scijava.Prioritized;
import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;

/**
 * Abstract superclass for Swing-based user interfaces.
 *
 * @author Curtis Rueden
 * @author Lee Kamentsky
 * @author Barry DeZonia
 * @author Grant Harris
 */
public class KNIPSwingUI extends AbstractContextual implements UserInterface {

    @Parameter
    private AppEventService appEventService;

    @Parameter
    private EventService eventService;

    @Parameter
    private MenuService menuService;

    @Parameter
    private UIService uiService;

    private JPanel content;

    private SwingToolBar toolBar;

    private SwingStatusBar statusBar;

    private AWTClipboard systemClipboard;

    public KNIPSwingUI(final Context c) {
        setContext(c);
    }

    // -- UserInterface methods --

    @Override
    public SwingToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public SwingStatusBar getStatusBar() {
        return statusBar;
    }

    @Override
    public SystemClipboard getSystemClipboard() {
        return systemClipboard;
    }

    @Override
    public void showContextMenu(final String menuRoot, final Display<?> display, final int x, final int y) {
        final ShadowMenu shadowMenu = menuService.getMenu(menuRoot);

        final JPopupMenu popupMenu = new JPopupMenu();
        new SwingJPopupMenuCreator().createMenus(shadowMenu, popupMenu);

        final DisplayViewer<?> displayViewer = uiService.getDisplayViewer(display);
        if (displayViewer != null) {
            final Component invoker = (Component)displayViewer.getPanel();
            popupMenu.show(invoker, x, y);
        }
    }

    @Override()
    public boolean requiresEDT() {
        return true;
    }

    // -- Disposable methods --

    @Override
    public void dispose() {
    }

    // -- Internal methods --

    protected JPanel createUI() {
        menuService = getContext().getService(DefaultMenuService.class);

        final JMenuBar menuBar = createMenus();

        content = new JPanel();

        if (menuBar != null) {
            content.add(menuBar);
        }

        toolBar = new SwingToolBar(getContext());
        statusBar = new SwingStatusBar(getContext());
        systemClipboard = new AWTClipboard();

        content.add(toolBar);
        content.add(statusBar);

        // listen for input events on all components of the app frame
        final AWTInputEventDispatcher inputDispatcher = new AWTInputEventDispatcher(null, eventService);
        inputDispatcher.register(content, true, true);

        // listen for drag and drop events
        final AWTDropTargetEventDispatcher dropTargetDispatcher = new AWTDropTargetEventDispatcher(null, eventService);
        dropTargetDispatcher.register(toolBar);
        dropTargetDispatcher.register(statusBar);
        dropTargetDispatcher.register(content);

        return content;
    }

    /**
     * Creates a {@link JMenuBar} from the master {@link ShadowMenu} structure.
     */
    protected JMenuBar createMenus() {
        final JMenuBar menuBar = menuService.createMenus(new SwingJMenuBarCreator(), new JMenuBar());
        final AppMenusCreatedEvent appMenusCreatedEvent = new AppMenusCreatedEvent(menuBar);
        eventService.publish(appMenusCreatedEvent);
        if (appMenusCreatedEvent.isConsumed()) {
            // something else (e.g., MacOSXPlatform) handled the menus
            return null;
        }
        appMenusCreatedEvent.consume();
        return menuBar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPriority() {
        // TODO Auto-generated method stub
        return 100;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPriority(final double priority) {
        //
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Prioritized arg0) {
        return (int)(getPriority() - arg0.getPriority());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        content.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        return content.isVisible();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(final Object o) {
        if (o instanceof JComponent) {
            content.add((Component)o);
            ((JComponent)o).updateUI();
        } else {
            throw new IllegalArgumentException("not supported");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(final String name, final Object o) {
        show(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(final Display<?> display) {
        throw new IllegalArgumentException("not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Desktop getDesktop() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationFrame getApplicationFrame() {
        return new ApplicationFrame() {

            @Override
            public void setLocation(final int x, final int y) {
                //
            }

            @Override
            public int getLocationX() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getLocationY() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public void activate() {
                //
            }

            @Override
            public void setVisible(final boolean visible) {
                content.setVisible(true);
            }

        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayWindow createDisplayWindow(final Display<?> display) {
        return new DisplayWindow() {

            @Override
            public void showDisplay(final boolean visible) {
                content.setVisible(true);
            }

            @Override
            public void setTitle(final String s) {
                //
            }

            @Override
            public void setContent(final DisplayPanel panel) {
                if (panel instanceof SwingDisplayPanel) {
                    content.add((SwingDisplayPanel)panel);
                }
            }

            @Override
            public void requestFocus() {
                //
            }

            @Override
            public void pack() {
            }

            @Override
            public int findDisplayContentScreenY() {
                return 0;
            }

            @Override
            public int findDisplayContentScreenX() {
                return 0;
            }

            @Override
            public void close() {
            }
        };
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
    public File chooseFile(final File file, final String style) {
        throw new UnsupportedOperationException("file chooser not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveLocation() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreLocation() {

    }

}
