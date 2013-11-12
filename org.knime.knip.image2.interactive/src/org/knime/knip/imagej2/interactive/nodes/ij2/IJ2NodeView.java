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
 * --------------------------------------------------------------------- *
 *
 */
package org.knime.knip.imagej2.interactive.nodes.ij2;

import imagej.data.Dataset;
import imagej.data.DefaultDataset;
import imagej.data.autoscale.AutoscaleService;
import imagej.data.display.DefaultDatasetView;
import imagej.data.display.DefaultOverlayService;
import imagej.display.DisplayService;
import imagej.service.ImageJService;
import imagej.ui.UIService;
import imagej.ui.swing.overlay.JHotDrawService;
import imagej.widget.WidgetService;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataCell;
import org.knime.core.data.RowKey;
import org.knime.core.node.interactive.DefaultReexecutionCallback;
import org.knime.core.node.interactive.InteractiveClientNodeView;
import org.knime.core.node.tableview.TableContentModel;
import org.knime.core.node.tableview.TableContentView;
import org.knime.core.node.tableview.TableView;
import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.imagej2.interactive.nodes.ij2.swing.KNIPSwingMdiUI;
import org.scijava.Context;

public class IJ2NodeView<T extends RealType<T> & NativeType<T>> extends
        InteractiveClientNodeView<IJ2NodeModel<T>, IJ2ViewContent> implements ListSelectionListener {

    private IJ2NodeModel<T> m_model;

    private IJ2ViewContent m_viewContent;

    private Context m_context;

    private KNIPSwingMdiUI m_ui;

    private int m_currentRow;

    private TableContentView m_tableContentView;

    /**
     * @param nodeModel
     */
    protected IJ2NodeView(final IJ2NodeModel<T> nodeModel) {
        super(nodeModel);
        m_model = nodeModel;

        if (m_viewContent != null) {
            initGUI();
        } else {
            final JLabel load = new JLabel("Waiting for first execution ...");
            load.setPreferredSize(new Dimension(500, 500));
            setComponent(load);
        }

        // init
        m_currentRow = -1;

        // Init IJ2 here
        m_context =
                new Context(ImageJService.class, AutoscaleService.class, JHotDrawService.class,
                        DefaultOverlayService.class, WidgetService.class);
    }

    /**
     *
     */
    private void initGUI() {
        JPanel viewPanel = new JPanel();

        m_tableContentView = new TableContentView();
        m_tableContentView.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_tableContentView.getSelectionModel().addListSelectionListener(this);
        m_tableContentView.getColumnModel().getSelectionModel().addListSelectionListener(this);
        TableView tableView = new TableView(m_tableContentView);

        // UIService doesn't work
        UIService service = m_context.getService(UIService.class);

        // try to get it
        m_ui = (KNIPSwingMdiUI)service.getUI(KNIPSwingMdiUI.NAME);
        m_ui.show();

        // split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(tableView);
        splitPane.add(m_ui.getApplicationFrame());
        splitPane.setDividerLocation(300);

        viewPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        viewPanel.add(splitPane, gbc);

        JButton executeButton = new JButton("Rexecute");
        executeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                saveCurrent();
//                m_model.loadViewContent(m_viewContent);
                triggerReExecution(m_viewContent, new DefaultReexecutionCallback());
            }
        });

        viewPanel.add(executeButton);

        setComponent(viewPanel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
//        triggerReExecution(m_viewContent, new DefaultReexecutionCallback());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        m_viewContent = m_model.createViewContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        m_viewContent = m_model.createViewContent();
        if (m_viewContent != null) {
            initGUI();
            m_tableContentView.setModel(new TableContentModel(m_viewContent.table()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateModel(final Object arg) {
        modelChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(final ListSelectionEvent e) {

        if (e.getValueIsAdjusting()) {
            return;
        }

        final int col = m_tableContentView.getColumnModel().getSelectionModel().getLeadSelectionIndex();

        if (col != m_viewContent.imgIdx()) {
            throw new IllegalArgumentException("Only processing of single images is supported since now!");
        }

        final int row = m_tableContentView.getSelectionModel().getLeadSelectionIndex();

        if (row == m_currentRow) {
            return;
        } else {

            if (m_currentRow != -1) {
                saveCurrent();
            }
        }
        m_currentRow = row;

        final DataCell currentImgCell =
                m_tableContentView.getContentModel().getValueAt(m_currentRow, m_viewContent.imgIdx());

        @SuppressWarnings("unchecked")
        ImgPlus<T> imgPlus = ((ImgPlusValue<T>)currentImgCell).getImgPlus();

        m_ui.show(new DefaultDataset(m_context, imgPlus));
    }

    /**
     *
     */
    private void saveCurrent() {
        RowKey rowKey = ((TableContentModel)m_tableContentView.getModel()).getRowKey(m_currentRow);
        Dataset object =
                ((DefaultDatasetView)m_context.getService(DisplayService.class).getActiveDisplay().get(0)).getData();
        m_viewContent.get().put(rowKey, object.getImgPlus());
    }
}
