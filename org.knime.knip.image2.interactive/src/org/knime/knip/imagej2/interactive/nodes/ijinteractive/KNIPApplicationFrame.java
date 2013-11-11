/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * Created on 11.11.2013 by Christian Dietz
 */
package org.knime.knip.imagej2.interactive.nodes.ijinteractive;

import imagej.ui.ApplicationFrame;
import imagej.ui.common.awt.AWTInputEventDispatcher;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JInternalFrame;

/**
 *
 * @author Christian Dietz
 */
public class KNIPApplicationFrame extends JInternalFrame implements ApplicationFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public KNIPApplicationFrame(final String string) {
        this.setTitle(string);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocationX() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocationY() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(final int x, final int y) {
        // nope
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
    }

    // -- SwingApplicationFrame methods --
    public void addEventDispatcher(final AWTInputEventDispatcher dispatcher) {
        dispatcher.register(this, false, true);
        addKeyDispatcher(dispatcher, getContentPane());
    }

    /** Recursively listens for keyboard events on the given component. */
    private void addKeyDispatcher(final AWTInputEventDispatcher dispatcher, final Component comp) {
        comp.addKeyListener(dispatcher);
        if (!(comp instanceof Container)) {
            return;
        }
        final Container c = (Container)comp;
        final int childCount = c.getComponentCount();
        for (int i = 0; i < childCount; i++) {
            final Component child = c.getComponent(i);
            addKeyDispatcher(dispatcher, child);
        }
    }
}