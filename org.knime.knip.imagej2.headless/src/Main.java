import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.osgi.framework.internal.core.Constants;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;

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
 * Created on Nov 12, 2013 by dietzc
 */

/**
 * 
 * @author dietzc
 */
public class Main {

        public static final String KNIME_DIR = "C:\\Users\\Christian Dietz\\Desktop\\devel\\KNIME 2.8.2\\";

        public static void main(final String[] args) throws Exception {
                // set external parameters
                int imgId = 64;

                String[] props = new String[] {"-startup", KNIME_DIR + "plugins\\org.eclipse.equinox.launcher_1.2.0.v20110502.jar",
                                "--launcher.library", KNIME_DIR + "plugins\\org.eclipse.equinox.launcher.win32.win32.x86_64_1.1.100.v20110502",
                                "-application", "org.knime.product.KNIME_BATCH_APPLICATION", "-workflowDir=\"ij2_headless_test\""};

                if (FrameworkProperties.getProperty("eclipse.startTime") == null) {
                        FrameworkProperties.setProperty("eclipse.startTime", Long.toString(System.currentTimeMillis())); //$NON-NLS-1$
                }
                if (FrameworkProperties.getProperty(EclipseStarter.PROP_NOSHUTDOWN) == null) {
                        FrameworkProperties.setProperty(EclipseStarter.PROP_NOSHUTDOWN, "true"); //$NON-NLS-1$
                }
                // set the compatibility boot delegation flag to false to get "standard" OSGi behavior WRT boot delegation (bug 178477)
                if (FrameworkProperties.getProperty(Constants.OSGI_COMPATIBILITY_BOOTDELEGATION) == null) {
                        FrameworkProperties.setProperty(Constants.OSGI_COMPATIBILITY_BOOTDELEGATION, "false"); //$NON-NLS-1$
                }

                Object result = EclipseStarter.run(props, null);
                if (result instanceof Integer && !Boolean.valueOf(FrameworkProperties.getProperty(EclipseStarter.PROP_NOSHUTDOWN)).booleanValue()) {
                        System.exit(((Integer) result).intValue());
                }

                //                // add img to local repository and load it
                //                IJMemoryReaderNodeModel.remoteImgPlus = new ImgPlus(new ArrayImgFactory<FloatType>().create(new long[] {100, 100}, new FloatType()));
                //
                //                // set everything to one
                //                Cursor<FloatType> cursor = IJMemoryReaderNodeModel.remoteImgPlus.cursor();
                //                while (cursor.hasNext()) {
                //                        cursor.get().setReal(1);
                //                }
                //
                //                // check whether everything is inverted
                //                cursor = IJMemoryWriterNodeModel.remoteImgPlus.cursor();
                //                System.out.println("Result " + cursor.next().getRealDouble());
        }
}
