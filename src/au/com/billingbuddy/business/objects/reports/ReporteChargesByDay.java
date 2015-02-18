package au.com.billingbuddy.business.objects.reports;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.exceptions.objects.ReporteChargesByDayException;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class ReporteChargesByDay {
	
	private static int dimensionXScreen = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.dimensionXScreen"));
	private static int dimensionYScreen = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.dimensionYScreen"));
	private static int adjustmentDimensionYScreen = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.adjustmentDimensionYScreen"));
	
	private double initialXPositionGrahic = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.initialXPositionGrahic"));
	private double initialYPositionGrahic = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.initialYPositionGrahic"));
	
	private double longYGrahic = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.longYGrahic"));
	
	private double escalaX;
	private double mayorY;
	private double minorY;
	private double mayorX;
	
	private static ReporteChargesByDay instance = null;
	
	private double rightMargenReferenceLine = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.rightMargenReferenceLine"));
	private double leftMargenReferenceLine = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.leftMargenReferenceLine"));
	
	private double rightMargenGrahic = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.rightMargenGrahic"));
	private double leftMargenGrahic = Integer.parseInt(ConfigurationSystem.getInstance().getKey("report.leftMargenGrahic"));
	private double scaleYFactor = initialYPositionGrahic + longYGrahic;
	private double scaleXFactor = dimensionXScreen - (leftMargenGrahic + rightMargenGrahic);
	
	private double longXGrahic = dimensionXScreen-(leftMargenGrahic + rightMargenGrahic);
	
	public static synchronized ReporteChargesByDay getInstance() {
		if (instance == null) {
			instance = new ReporteChargesByDay();
		}
		return instance;
	}
	
	private ReporteChargesByDay() {}
	
	public StringWriter CreateXml(ArrayList<TransactionVO> listaReport) throws ReporteChargesByDayException {
		DOMSource domSource = null;
		try {
			if(listaReport.size() > 0){
				TransactionVO transactionVOMAX = Collections.max(listaReport,new SortListByChargesDesc());
				TransactionVO transactionVOMIN = Collections.max(listaReport,new SortListByChargesDesc());
				mayorY = Integer.parseInt(transactionVOMAX.getTotalDateReport());
				minorY = Integer.parseInt(transactionVOMIN.getTotalDateReport());
				
				if (mayorY == minorY) minorY = 0;
				mayorX = listaReport.size() + 1;
				escalaX = longXGrahic / (listaReport.size() + 1);
				
//				System.out.println("mayorY: " +  mayorY);
//				System.out.println("minorY: " +  minorY);
//				System.out.println("mayorX: " +  mayorX);
//				System.out.println("escalaX: " +  escalaX);
//				System.out.println("scaleYFactor: " +  scaleYFactor);
//				System.out.println("scaleXFactor: " +  scaleXFactor);
//				System.out.println("longXGrahic: " + longXGrahic);
				
				DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("grafica");
				document.appendChild(rootElement);
				
				double positionX = leftMargenGrahic + escalaX;
//				System.out.println("positionX: " + positionX);
				double positionY = 0;
				String path = "";
				Collections.sort(listaReport,new SortListByDate());
				for (TransactionVO transactionVO : listaReport) {
					positionY = scaleYFactor - scaleValue(longYGrahic, Double.parseDouble(transactionVO.getTotalDateReport()));
//					System.out.println("("+transactionVO.getDateReport()+" , "+transactionVO.getTotalDateReport()+") - ("+positionX+" , "+ positionY +")");
					path += "L"+positionX +","+ positionY + " ";
					
					Element point = document.createElement("point");
					rootElement.appendChild(point);
									
					Element pointx = document.createElement("pointx");
					pointx.appendChild(document.createTextNode(String.valueOf(positionX)));
					point.appendChild(pointx);
					
					Element pointxReference = document.createElement("pointxReference");
					pointxReference.appendChild(document.createTextNode(String.valueOf(positionX+10)));
					point.appendChild(pointxReference);
					
					
					Element pointy = document.createElement("pointy");
					pointy.appendChild(document.createTextNode(String.valueOf(positionY)));
					point.appendChild(pointy);
					
					Element label = document.createElement("label");
					label.appendChild(document.createTextNode("("+transactionVO.getDateReport()+" ,"+transactionVO.getTotalDateReport()+")"));
					point.appendChild(label);
					
					Element date = document.createElement("date");
					date.appendChild(document.createTextNode(Utilities.formatDate(transactionVO.getDateReport(), 2, 4)));
					point.appendChild(date);
					
					positionX += escalaX;
					
				}
				
				positionX -= escalaX;
				
				Element dimensionX = document.createElement("dimensionX");
				rootElement.appendChild(dimensionX);
				
				Element value = document.createElement("value");
				value.appendChild(document.createTextNode(String.valueOf(dimensionXScreen)));
				dimensionX.appendChild(value);
				
				Element dimensionY = document.createElement("dimensionY");
				rootElement.appendChild(dimensionY);
				
				value = document.createElement("value");
				value.appendChild(document.createTextNode(String.valueOf(dimensionYScreen)));
				dimensionY.appendChild(value);
				
				path = "M" + (leftMargenGrahic + escalaX)+","+scaleYFactor + " "+path + "L"+positionX+","+scaleYFactor;
				
				Element pathLinearGradient = document.createElement("pathLinearGradient");
				rootElement.appendChild(pathLinearGradient);
				value = document.createElement("value");
				value.appendChild(document.createTextNode(path));
				pathLinearGradient.appendChild(value);
				
				/* Init HighestReference */
				positionY = scaleYFactor - scaleValue(longYGrahic, mayorY);
				Element highestReference = document.createElement("highestReference");
				rootElement.appendChild(highestReference);
				
				Element firstPoint = document.createElement("firtPointHighestReference");
				firstPoint.appendChild(document.createTextNode("M" + leftMargenReferenceLine +","+ (positionY)));
				highestReference.appendChild(firstPoint);
				
				Element secondPoint = document.createElement("secondPointHighestReference");
				secondPoint.appendChild(document.createTextNode("L"+(dimensionXScreen - rightMargenReferenceLine) +","+ (positionY)));
				highestReference.appendChild(secondPoint);
	
				Element positionLabelHighestReference = document.createElement("positionLabelHighestReference");
				rootElement.appendChild(positionLabelHighestReference);
				
				value = document.createElement("X");
				value.appendChild(document.createTextNode(String.valueOf(dimensionXScreen - rightMargenReferenceLine - 10)));
				positionLabelHighestReference.appendChild(value);
				
				value = document.createElement("Y");
				value.appendChild(document.createTextNode(String.valueOf(positionY - 10)));
				positionLabelHighestReference.appendChild(value);
				
				value = document.createElement("value");
				value.appendChild(document.createTextNode(String.valueOf(mayorY)));
				positionLabelHighestReference.appendChild(value);
				
				/* Finish HighestReference */
				
				/* Init MiddleReference */
				positionY = ((scaleYFactor - scaleValue(longYGrahic, minorY)) + (scaleYFactor - scaleValue(longYGrahic, mayorY)))/2;
				Element middleReference = document.createElement("middleReference");
				rootElement.appendChild(middleReference);
				
				firstPoint = document.createElement("firtPointMiddleReference");
				firstPoint.appendChild(document.createTextNode("M" + leftMargenReferenceLine +","+ (positionY)));
				middleReference.appendChild(firstPoint);
				
				secondPoint = document.createElement("secondPointMiddleReference");
				secondPoint.appendChild(document.createTextNode("L"+(dimensionXScreen - rightMargenReferenceLine) +","+ (positionY)));
				middleReference.appendChild(secondPoint);
				
				Element positionLabelMiddleReference = document.createElement("positionLabelMiddleReference");
				rootElement.appendChild(positionLabelMiddleReference);
				
				value = document.createElement("X");
				value.appendChild(document.createTextNode(String.valueOf(dimensionXScreen - rightMargenReferenceLine - 10)));
				positionLabelMiddleReference.appendChild(value);
				
				value = document.createElement("Y");
				value.appendChild(document.createTextNode(String.valueOf(positionY - 10)));
				positionLabelMiddleReference.appendChild(value);
				
				value = document.createElement("value");
				value.appendChild(document.createTextNode(String.valueOf((mayorY - minorY)/2)));
				positionLabelMiddleReference.appendChild(value);
				
				/* Finish MiddleReference */
				
				
				/* Init LessReference */
				positionY = scaleYFactor - scaleValue(longYGrahic, minorY);
				Element lessReference = document.createElement("lessReference");
				rootElement.appendChild(lessReference);
				
				firstPoint = document.createElement("firtPointLessReference");
				firstPoint.appendChild(document.createTextNode("M" + (leftMargenReferenceLine)+","+ (positionY)));
				lessReference.appendChild(firstPoint);
				
				secondPoint = document.createElement("secondPointLessReference");
				secondPoint.appendChild(document.createTextNode("L"+(dimensionXScreen - rightMargenReferenceLine) +","+ (positionY)));
				lessReference.appendChild(secondPoint);
				
				Element positionLabelLessReference = document.createElement("positionLabelLessReference");
				rootElement.appendChild(positionLabelLessReference);
				
				value = document.createElement("X");
				value.appendChild(document.createTextNode(String.valueOf(dimensionXScreen - rightMargenReferenceLine - 10)));
				positionLabelLessReference.appendChild(value);
				
				value = document.createElement("Y");
				value.appendChild(document.createTextNode(String.valueOf(positionY - 10)));
				positionLabelLessReference.appendChild(value);
				
				value = document.createElement("value");
				value.appendChild(document.createTextNode(String.valueOf(minorY)));
				positionLabelLessReference.appendChild(value);
				
				/* Finish LessReference */
				
				positionY = scaleYFactor - scaleValue(longYGrahic, minorY) + 20;
				Element scaleXReference = document.createElement("scaleXReference");
				rootElement.appendChild(scaleXReference);
				value = document.createElement("value");
				value.appendChild(document.createTextNode(String.valueOf(positionY)));
				scaleXReference.appendChild(value);
				
	//			// creating and writing to xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
	//			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				domSource = new DOMSource(document);
				
	//			printContent(domSource);
//				printDocument(domSource);
				return printDocument(domSource);
			}else{
				return new StringWriter().append("There are not information to show");
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			ReporteChargesByDayException reporteChargesByDayException = new ReporteChargesByDayException(e);
			throw reporteChargesByDayException;
		} catch (TransformerException e) {
			e.printStackTrace();
			ReporteChargesByDayException reporteChargesByDayException = new ReporteChargesByDayException(e);
			throw reporteChargesByDayException;
		}
	}
	
	public StringWriter printDocument(DOMSource source) throws TransformerConfigurationException, TransformerException {
		String inputXSL = "file:///run/media/Edicson/SVG%20Example/ejemploGrafica/grafica.xsl";
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource xslStream = new StreamSource(inputXSL);
		Transformer transformer = factory.newTransformer(xslStream);
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		StringWriter sw = new StringWriter();
		StreamResult out = new StreamResult(sw);
		transformer.transform(source, out);
//		System.out.println("************************ Inicio Documento Generado ************************ ");
//		System.out.println(sw.toString());
//		System.out.println("************************ Fin Documento Generado ************************ ");
		return sw;
	}
	
	public void printContent(DOMSource source){
		try {
			System.out.println("Imprimiendo...");
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
//			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			// Print the DOM node

			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			
			trans.transform(source, result);
			String xmlString = sw.toString();

			System.out.println("xmlString: \n" + xmlString);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public double scaleValue(double dimensionScreen, double value){
		return (value * dimensionScreen)/mayorY;
	}
	
	class SortListByChargesDesc implements Comparator<TransactionVO>{
		public int compare(TransactionVO transactionVOA, TransactionVO transactionVOB) {
			return Integer.parseInt(transactionVOA.getTotalDateReport()) < Integer.parseInt(transactionVOB.getTotalDateReport()) ? -1 : Integer.parseInt(transactionVOA.getTotalDateReport()) == Integer.parseInt(transactionVOB.getTotalDateReport()) ? 0 : 1;
		}
	}
	
	class SortListByChargesAsc implements Comparator<TransactionVO>{
		public int compare(TransactionVO transactionVOA, TransactionVO transactionVOB) {
			return Integer.parseInt(transactionVOA.getTotalDateReport()) > Integer.parseInt(transactionVOB.getTotalDateReport()) ? -1 : Integer.parseInt(transactionVOA.getTotalDateReport()) == Integer.parseInt(transactionVOB.getTotalDateReport()) ? 0 : 1;
		}
	}
	
	class SortListByDate implements Comparator<TransactionVO>{
		public int compare(TransactionVO transactionVOA, TransactionVO transactionVOB) {
			if(Utilities.stringToDate(transactionVOA.getDateReport(),2).compareTo(Utilities.stringToDate(transactionVOB.getDateReport(),2)) > 0){
        		return 1;
			}else if(Utilities.stringToDate(transactionVOA.getDateReport(),2).compareTo(Utilities.stringToDate(transactionVOB.getDateReport(),2)) < 0){
        		return -1; 
			
			}else if(Utilities.stringToDate(transactionVOA.getDateReport(),2).compareTo(Utilities.stringToDate(transactionVOB.getDateReport(),2)) == 0){
        		return 0;
        	}
			return 1;
		}
		
	}
	
}
