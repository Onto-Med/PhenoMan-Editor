package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class TreeTransferHandler extends TransferHandler {
	public static final DataFlavor FLAVOR = new ActivationDataFlavor(
		DefaultMutableTreeNode.class,
		DataFlavor.javaJVMLocalObjectMimeType,
		"DefaultMutableTreeNode"
	);

	@Override
	protected Transferable createTransferable(JComponent c) {
		JTree    source = (JTree) c;
		TreePath path   = source.getSelectionPath();

		assert path != null;
		return new DataHandler(path.getLastPathComponent(), FLAVOR.getMimeType());
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}
}
