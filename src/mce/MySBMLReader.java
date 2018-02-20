package mce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

/**
 * The SBMLReader readSBML prints the file path which was annoying. I suppress
 * their output stream
 * 
 * @author memin
 *
 */
public class MySBMLReader extends SBMLReader {
	private static final long serialVersionUID = 1L;

	@Override
	public SBMLDocument readSBML(String fileName) throws XMLStreamException, IOException {
		PrintStream originalStream = System.out;
		System.setOut(new NullPrintStream());
		SBMLDocument sbmlDocument = super.readSBML(fileName);
		System.setOut(originalStream);
		return sbmlDocument;
	}

}

class NullPrintStream extends PrintStream {

	public NullPrintStream() {
		super(new NullByteArrayOutputStream());
	}

	private static class NullByteArrayOutputStream extends ByteArrayOutputStream {

		@Override
		public void write(int b) {
			// do nothing
		}

		@Override
		public void write(byte[] b, int off, int len) {
			// do nothing
		}

		@Override
		public void writeTo(OutputStream out) throws IOException {
			// do nothing
		}

	}

}
