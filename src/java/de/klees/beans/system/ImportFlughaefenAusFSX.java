/* Fly the World
 * Wirtschaftsymulation für Flugsimulatoren
 * Copyright (C) 2016 Stefan Klees
 * stefan.klees@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.klees.beans.system;

import de.klees.beans.airport.AirportFacade;
import de.klees.data.Airport;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stefan Klees
 */
@Named(value = "importFlughaefenAusFSX")
@ViewScoped
public class ImportFlughaefenAusFSX implements Serializable {

  private static final long serialVersionUID = 1L;
  private final List Textlist = new ArrayList();
  private String ICAO;
  private String Country;
  private String State;
  private String City;
  private String Name;
  private String Latitude;
  private String Longitude;
  private String Hoehe;
  private String Laenge;
  private String Belag;

  @EJB
  AirportFacade facadeAirport;
  Airport newAirport;

//  public ImportFlughaefenAusFSX() {
//  }
//
////<data>
////  <ICAO id="00AL">
////    <ICAOName>Epps Airpark</ICAOName>
////    <Country>United States</Country>
////    <State>Alabama</State>
////    <City>Harvest</City>
////    <Longitude>-86.770279</Longitude>
////    <Latitude>34.864811</Latitude>
////    <Altitude>820</Altitude>
////    <MagVar>0.000</MagVar>
////      <Runway id="01">
////        <Len>2300</Len>
////        <Hdg>10.000</Hdg>
////        <Def>Grass</Def>
////      </Runway> 
////  </ICAO>
////</data>  
//  public void onXmlLesen2Versuch() {
//
//    try {
//
//      File fXmlFile = new File("/home/stefan/temp/schrott/runways1.xml");
//      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//      Document doc = dBuilder.parse(fXmlFile);
//
//      //optional, but recommended
//      //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//      doc.getDocumentElement().normalize();
//
//      System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//      NodeList nList = doc.getElementsByTagName("ICAO");
//
//      System.out.println("----------------------------");
//
//      for (int temp = 0; temp < nList.getLength(); temp++) {
//
//        Node nNode = nList.item(temp);
//
//        //System.out.println("\nCurrent Element :" + nNode.getNodeName());
//        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//          Element eElement = (Element) nNode;
//
//          System.out.println("Icao : " + eElement.getAttribute("id"));
//          System.out.println("Name : " + eElement.getElementsByTagName("ICAOName").item(0).getTextContent());
//          System.out.println("Country : " + eElement.getElementsByTagName("Country").item(0).getTextContent());
//          System.out.println("State : " + eElement.getElementsByTagName("State").item(0).getTextContent());
//          System.out.println("City : " + eElement.getElementsByTagName("City").item(0).getTextContent());
//          System.out.println("Altitude : " + eElement.getElementsByTagName("Altitude").item(0).getTextContent());
//          System.out.println("Latitude : " + eElement.getElementsByTagName("Latitude").item(0).getTextContent());
//          System.out.println("Longitude : " + eElement.getElementsByTagName("Longitude").item(0).getTextContent());
//
//          if (facadeAirport.findAllbyIcaoSingle(eElement.getAttribute("id")).size() <= 0) {
//
//            newAirport = new Airport();
//            newAirport.setIcao(eElement.getAttribute("id"));
//            newAirport.setCity(eElement.getElementsByTagName("City").item(0).getTextContent());
//            newAirport.setCountry(eElement.getElementsByTagName("Country").item(0).getTextContent());
//            newAirport.setElevation(Integer.valueOf(eElement.getElementsByTagName("Altitude").item(0).getTextContent()));
//            newAirport.setKaufpreis(275000.0);
//            newAirport.setLatitude(Double.valueOf(eElement.getElementsByTagName("Latitude").item(0).getTextContent()));
//            newAirport.setLongitude(Double.valueOf(eElement.getElementsByTagName("Longitude").item(0).getTextContent()));
//            newAirport.setName(eElement.getElementsByTagName("ICAOName").item(0).getTextContent());
//            newAirport.setState(eElement.getElementsByTagName("State").item(0).getTextContent());
//            newAirport.setSurfacetype(1);
//
//            facadeAirport.create(newAirport);
//          }
//
//        }
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void onXmlLesesen3Versuch() {
//
//    try {
//
//      File file = new File("/home/stefan/temp/schrott/runways1.xml");
//
//      DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//
//      Document doc = dBuilder.parse(file);
//
//      System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//      if (doc.hasChildNodes()) {
//
//        printNote(doc.getChildNodes());
//
//      }
//
//    } catch (Exception e) {
//      System.out.println(e.getMessage());
//    }
//
//  }
//
//  private static void printNote(NodeList nodeList) {
//
//    for (int count = 0; count < nodeList.getLength(); count++) {
//
//      Node tempNode = nodeList.item(count);
//
//      // make sure it's element node.
//      if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
//
//        // get node name and value
//        System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
//        System.out.println("Node Value =" + tempNode.getTextContent());
//
//        if (tempNode.hasAttributes()) {
//
//          // get attributes names and values
//          NamedNodeMap nodeMap = tempNode.getAttributes();
//
//          for (int i = 0; i < nodeMap.getLength(); i++) {
//
//            Node node = nodeMap.item(i);
//            System.out.println("attr name : " + node.getNodeName());
//            System.out.println("attr value : " + node.getNodeValue());
//
//          }
//
//        }
//
//        if (tempNode.hasChildNodes()) {
//
//          // loop again if has child nodes
//          printNote(tempNode.getChildNodes());
//
//        }
//
//        System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
//
//      }
//
//    }
//
//  }
//
//  public void onXmlLesen() {
//
//    try {
//
//      SAXParserFactory factory = SAXParserFactory.newInstance();
//      SAXParser saxParser = factory.newSAXParser();
//
//      DefaultHandler handler = new DefaultHandler() {
//
//        boolean boIcao = false;
//        boolean boCountry = false;
//        boolean boState = false;
//        boolean boCity = false;
//        boolean boName = false;
//        boolean boLatitude = false;
//        boolean boLongitude = false;
//        boolean boHoehe = false;
//        boolean boLaenge = false;
//        boolean boBelag = false;
//
//        @Override
//        public void startElement(String uri, String localName, String qName,
//                Attributes attributes) throws SAXException {
//
//          //System.out.println("Start Element :" + qName);
//          if (qName.equalsIgnoreCase("ICAO id")) {
//            boIcao = true;
//          }
//
//          if (qName.equalsIgnoreCase("ICAOName")) {
//            boName = true;
//          }
//
//          if (qName.equalsIgnoreCase("State")) {
//            boState = true;
//          }
//
//          if (qName.equalsIgnoreCase("Longitude")) {
//            boLongitude = true;
//          }
//
//          if (qName.equalsIgnoreCase("Latitude")) {
//            boLatitude = true;
//          }
//
//          if (qName.equalsIgnoreCase("Altitude")) {
//            boHoehe = true;
//          }
//          if (qName.equalsIgnoreCase("Len")) {
//            boLaenge = true;
//          }
//          if (qName.equalsIgnoreCase("Def")) {
//            boBelag = true;
//          }
//
//        }
//
//        @Override
//        public void endElement(String uri, String localName,
//                String qName) throws SAXException {
//
//          //System.out.println("End Element :" + qName);
//          try {
//
//            if (qName.equals("ICAO")) {
//              System.out.println("ICAO : " + ICAO);
////              System.out.println("Name : " + Name);
////              System.out.println("City : " + City);
////              System.out.println("Country : " + Country);
////              System.out.println("Belag : " + Belag);
////              System.out.println("Höhe : " + Hoehe);
////              System.out.println("Laenge : " + Laenge);
////              System.out.println("Latitude : " + Latitude);
////              System.out.println("Longitude : " + Longitude);
////              System.out.println("Staat : " + State);
//
//            }
//
//          } catch (NullPointerException e) {
//
//          }
//
//        }
//
//        @Override
//        public void characters(char ch[], int start, int length) throws SAXException {
//
//          if (boIcao) {
//            ICAO = new String(ch, start, length);
//            boIcao = false;
//          }
//
//          if (boBelag) {
//            Belag = new String(ch, start, length);
//            boBelag = false;
//          }
//
//          if (boCity) {
//            City = new String(ch, start, length);
//            boCity = false;
//          }
//
//          if (boCountry) {
//            Country = new String(ch, start, length);
//            boCountry = false;
//          }
//
//          if (boHoehe) {
//            Hoehe = new String(ch, start, length);
//            boHoehe = false;
//          }
//
//          if (boLaenge) {
//            Laenge = new String(ch, start, length);
//            boLaenge = false;
//          }
//
//          if (boLatitude) {
//            Latitude = new String(ch, start, length);
//            boLatitude = false;
//          }
//
//          if (boLongitude) {
//            Longitude = new String(ch, start, length);
//            boLongitude = false;
//          }
//
//          if (boName) {
//            Name = new String(ch, start, length);
//            boName = false;
//          }
//
//          if (boState) {
//            State = new String(ch, start, length);
//            boState = false;
//          }
//
//        }
//
//      };
//
//      saxParser.parse("/home/stefan/temp/schrott/runways.xml", handler);
//
//    } catch (ParserConfigurationException | SAXException | IOException e) {
//      e.printStackTrace();
//    }
//
//  }
//
//  public void onReadAirports() {
//    try {
//
//      File file = new File("/home/stefan/temp/schrott/runways.xml");
//
//      DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//
//      Document doc = dBuilder.parse(file);
//
//      System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//      if (doc.hasChildNodes()) {
//
//        printNote(doc.getChildNodes());
//
//      }
//
//    } catch (ParserConfigurationException | SAXException | IOException e) {
//      System.out.println(e.getMessage());
//    }
//
//  }
//
}
