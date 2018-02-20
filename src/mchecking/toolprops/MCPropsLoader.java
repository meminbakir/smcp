package mchecking.toolprops;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mce.util.Utils;
import mchecking.ModelChecker;

public class MCPropsLoader {
	private static Logger log = LoggerFactory.getLogger(MCPropsLoader.class);

	// private static String modelExtension = null;
	//
	// private static MCTypes type = null;
	// private static String name = null;
	// private static String appPath = null;
	// private static String outPutDir = null;
	// private static boolean hasExternalTool = false;
	// private static ExternalTool externalTool = null;
	// private static boolean usesPrism = true;
	// // Supported Patterns
	// private static List<Pattern> supportedPatterns = new ArrayList<>();
	/**
	 * This load Model Checker and its external tool(if exists) properties
	 * 
	 * @param type
	 */
	public static void loadMCProps(ModelChecker modelChecker) {
		try {
			File mcPropXML = new File("./MCProps.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document mcPropDoc = dBuilder.parse(mcPropXML);

			mcPropDoc.getDocumentElement().normalize();
			NodeList mcList = mcPropDoc.getElementsByTagName("mc");
			for (int i = 0; i < mcList.getLength(); i++) {
				Node mcNode = mcList.item(i);
				if (mcNode.getNodeType() == Node.ELEMENT_NODE) {

					Element mcElement = (Element) mcNode;
					// get model checker
					if (modelChecker.getType().toString().equals(mcElement.getAttribute("type"))) {
						modelChecker.setName(mcElement.getElementsByTagName("name").item(0).getTextContent());
						// get mc path, depends on the OS
						NodeList osList = ((Element) mcElement.getElementsByTagName("appPath").item(0))
								.getElementsByTagName("os");
						for (int j = 0; j < osList.getLength(); j++) {
							Node osNode = osList.item(j);
							if (osNode.getNodeType() == Node.ELEMENT_NODE) {
								Element osElement = (Element) osNode;
								// get the appPath, depending on the OS
								if (osElement.getAttribute("type").equals(Utils.getOS().toString())) {
									// set mc path
									modelChecker.setAppPath((osElement.getTextContent()).trim());
									break;
								}
							}
						}
//						modelChecker.setOutPutDir(mcElement.getElementsByTagName("outputDir").item(0).getTextContent());

						// If model has external tools, then initialize the external properties.
						String strHasExternalTool = mcElement.getAttribute("hasExternalTool");
						boolean hasExternalTool = false;
						if (strHasExternalTool != null)
							hasExternalTool = Boolean.valueOf(strHasExternalTool);

						if (hasExternalTool) {
							modelChecker.setHasExternalTool(hasExternalTool);
							Element externalToolElement = ((Element) (mcElement.getElementsByTagName("externalTool")
									.item(0)));
							// Initialize the external tool object
							ExternalTool externalToolObj = new ExternalTool();
							// set external model type, i.e., name
							externalToolObj.setExtTType(externalToolElement.getAttribute("type"));
							// get model extension of external tool
							externalToolObj.setModelExtension(
									externalToolElement.getElementsByTagName("modelExtension").item(0).getTextContent());
							// if it is searched external tool then get its path
							osList = ((Element) externalToolElement.getElementsByTagName("externalToolPath").item(0))
									.getElementsByTagName("os");
							for (int k = 0; k < osList.getLength(); k++) {
								Node osNode = osList.item(k);
								if (osNode.getNodeType() == Node.ELEMENT_NODE) {
									Element osElement = (Element) osNode;
									// get external tools path, depending on the OS
									if (osElement.getAttribute("type").equals(Utils.getOS().toString())) {
										externalToolObj.setExternalToolPath((osElement.getTextContent()).trim());
										break;
									}
								}
							}
							modelChecker.setExternalTool(externalToolObj);

						} else {
							// if mc doesn't has external tool, then it will have its own model extension.
							modelChecker.setModelExtension(
									mcElement.getElementsByTagName("modelExtension").item(0).getTextContent());
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /**
	// * This load Model Checker and its external tool(if exists) properties
	// *
	// * @param type
	// */
	// public static void loadMCProps(MCTypes type) {
	// try {
	// File mcPropXML = new File("./src/mchecking/toolprops/MCProps.xml");
	// DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	// DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	// Document mcPropDoc = dBuilder.parse(mcPropXML);
	//
	// mcPropDoc.getDocumentElement().normalize();
	// NodeList mcList = mcPropDoc.getElementsByTagName("mc");
	// for (int i = 0; i < mcList.getLength(); i++) {
	// Node mcNode = mcList.item(i);
	// if (mcNode.getNodeType() == Node.ELEMENT_NODE) {
	//
	// Element mcElement = (Element) mcNode;
	// // get model checker
	// if (type.toString().equals(mcElement.getAttribute("type"))) {
	// MCPropsLoader.type = type;
	// MCPropsLoader.setName(mcElement.getElementsByTagName("name").item(0).getTextContent());
	//
	// // get mc path, depends on the OS
	// NodeList osList = ((Element) mcElement.getElementsByTagName("appPath").item(0))
	// .getElementsByTagName("os");
	// for (int j = 0; j < osList.getLength(); j++) {
	// Node osNode = osList.item(j);
	// if (osNode.getNodeType() == Node.ELEMENT_NODE) {
	// Element osElement = (Element) osNode;
	// // get the appPath, depending on the OS
	// if (osElement.getAttribute("type").equals(Utils.getOS().toString())) {
	// // set mc path
	// MCPropsLoader.setAppPath((osElement.getTextContent()).trim());
	// break;
	// }
	// }
	// }
	//
	// // MCPropsLoader.setAppPath(mcElement.getElementsByTagName("appPath").item(0).getTextContent());
	// MCPropsLoader.setOutPutDir(mcElement.getElementsByTagName("outputDir").item(0).getTextContent());
	//
	// // If model has external tools, then initialize the external properties.
	// String strHasExternalTool = mcElement.getAttribute("hasExternalTool");
	// boolean hasExternalTool = false;
	// if (strHasExternalTool != null)
	// hasExternalTool = Boolean.valueOf(strHasExternalTool);
	//
	// if (hasExternalTool) {
	// MCPropsLoader.setHasExternalTool(hasExternalTool);
	// Element externalToolElement = ((Element) (mcElement.getElementsByTagName("externalTool")
	// .item(0)));
	// // Initialize the external tool object
	// ExternalTool externalToolObj = new ExternalTool();
	// // set external model type, i.e., name
	// externalToolObj.setExtTType(externalToolElement.getAttribute("type"));
	// // get model extension of external tool
	// externalToolObj.setModelExtension(
	// externalToolElement.getElementsByTagName("modelExtension").item(0).getTextContent());
	// // if it is searched external tool then get its path
	// osList = ((Element) externalToolElement.getElementsByTagName("externalToolPath").item(0))
	// .getElementsByTagName("os");
	// for (int k = 0; k < osList.getLength(); k++) {
	// Node osNode = osList.item(k);
	// if (osNode.getNodeType() == Node.ELEMENT_NODE) {
	// Element osElement = (Element) osNode;
	// // get external tools path, depending on the OS
	// if (osElement.getAttribute("type").equals(Utils.getOS().toString())) {
	// externalToolObj.setExternalToolPath((osElement.getTextContent()).trim());
	// break;
	// }
	// }
	// }
	// MCPropsLoader.setExternalTool(externalToolObj);
	//
	// } else {
	// // if mc doesn't has external tool, then it will have its own model extension.
	// MCPropsLoader.setModelExtension(
	// mcElement.getElementsByTagName("modelExtension").item(0).getTextContent());
	// }
	// break;
	// }
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public static MCTypes getType() {
	// return type;
	// }
	//
	// public static void setType(MCTypes type) {
	// MCPropsLoader.type = type;
	// }
	//
	// /**
	// * @return the name
	// */
	// public static String getName() {
	// return name;
	// }
	//
	// /**
	// * @param name
	// * the name to set
	// */
	// public static void setName(String name) {
	// if (name.isEmpty()) {
	// name = "NoNameMC";
	// log.warn(
	// "Model checker name has not been specified. Check mcProp.xml file. 'NoName4MC' has been set as defaul model checker
	// name.");
	// }
	// MCPropsLoader.name = name;
	// }
	//
	// /**
	// * @return the appPath
	// */
	// public static String getAppPath() {
	// return appPath;
	// }
	//
	// /**
	// * @param appPath
	// * the appPath to set
	// */
	// public static void setAppPath(String appPath) {
	// MCPropsLoader.appPath = appPath.trim();
	// }
	//
	// /**
	// * @return the outPutDir
	// */
	// public static String getOutPutDir() {
	// return outPutDir;
	// }
	//
	// /**
	// * @param outPutDir
	// * the outPutDir to set
	// */
	// public static void setOutPutDir(String outPutDir) {
	// if (outPutDir.isEmpty() || outPutDir == null) {
	// outPutDir = System.getProperty("user.dir") + File.separator + "models" + File.separator + "translated"
	// + File.separator + MCPropsLoader.getName();
	// // log.warn("The target output directory for '" + MCPropsLoader.getName()
	// // + "' model checker has not been specified. Check mcProp.xml file.\n Therefore '" + outPutDir
	// // + "' is used for default output directory.");
	// }
	// MCPropsLoader.outPutDir = outPutDir;
	// }
	//
	// /**
	// * @param modelExtension
	// * the modelExtension to set
	// */
	// public static void setModelExtension(String modelExtension) {
	// if (modelExtension.isEmpty()) {
	// modelExtension = "";
	// log.warn(
	// "MC model extension (eg. sm for prism), has not been specified. Check mcProp.xml file. No extension has been set as
	// default model checker name.");
	// }
	// MCPropsLoader.modelExtension = modelExtension;
	// }
	//
	// public static String getModelExtension() {
	//
	// return modelExtension;
	// }
	//
	// /**
	// * @return the hasExternalTool
	// */
	// public static boolean isHasExternalTool() {
	// return hasExternalTool;
	// }
	//
	// /**
	// * @param hasExternalTool
	// * the hasExternalTool to set
	// */
	// public static void setHasExternalTool(boolean hasExternalTool) {
	// MCPropsLoader.hasExternalTool = hasExternalTool;
	// }
	//
	// /**
	// * @return the externalTool
	// */
	// public static ExternalTool getExternalTool() {
	// return externalTool;
	// }
	//
	// /**
	// * @param externalTool
	// * the externalTool to set
	// */
	// public static void setExternalTool(ExternalTool externalTool) {
	// MCPropsLoader.externalTool = externalTool;
	// }
	//
	// /**
	// * Check either MCherker it self or its External tool uses PRISM, If it is so, we will check prism keywords. Currently
	// * only MC2 has choice to not use PRISM.
	// *
	// * @return the usesPrism
	// */
	// public static boolean isUsesPrism() {
	// usesPrism = true;
	// if (MCPropsLoader.type == MCTypes.MC2) {
	// if (MCPropsLoader.externalTool.getExtTType() != ExtTTypes.PRISM) {
	// usesPrism = false;
	// }
	// }
	// return usesPrism;
	// }
	//
	// /**
	// * @param usesPrism
	// * the usesPrism to set
	// */
	// public static void setUsesPrism(boolean usesPrism) {
	// MCPropsLoader.usesPrism = usesPrism;
	// }
	//
	// /**
	// * @return the MCs that can run this pattern
	// */
	// public List<Pattern> getSupportedPatterns() {
	// return this.supportedPatterns;
	// }

}
