package temperature.fileloader;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import temperature.annotation.FileLoaderProperty;
import temperature.fileloader.loaderinterface.IFileLoader;

@FileLoaderProperty(name = "choose")
public class ChooseFile implements IFileLoader {

	public File getFile() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
		jfc.setFileFilter(filter);
		jfc.setDialogTitle("Choose a text file: ");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isFile()) {
				return jfc.getSelectedFile();
			}
		}
		return null;
	}

}
