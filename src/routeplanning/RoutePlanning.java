/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeplanning;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/**
 *
 * @author xuhu7477
 */
//public class RoutePlanning {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//    }
//    
//}


import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;



public class RoutePlanning{
/* Dijkstra Algorithm
 * 
 *
 */
	public static void computePaths(Node source){
		source.shortestDistance=0;

		//implement a priority queue
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(source);

		while(!queue.isEmpty()){
			Node u = queue.poll();

			/*visit the adjacencies, starting from 
			the nearest node(smallest shortestDistance)*/
			
			for(Edge e: u.adjacencies){

				Node v = e.target;
				double weight = e.weight;

				//relax(u,v,weight)
				double distanceFromU = u.shortestDistance+weight;
				if(distanceFromU<v.shortestDistance){

					/*remove v from queue for updating 
					the shortestDistance value*/
					queue.remove(v);
					v.shortestDistance = distanceFromU;
					v.parent = u;
					queue.add(v);

				}
			}
		}
	}

	public static List<Node> getShortestPathTo(Node target){

		//trace path from target to source
		List<Node> path = new ArrayList<Node>();
		for(Node node = target; node!=null; node = node.parent){
			path.add(node);
		}


		//reverse the order such that it will be from source to target
		Collections.reverse(path);

		return path;
	}

		  public static String[] getManeuverList(List<Node> path){


				String[] maneuverlist = new String[path.size()-1];
				int start_index = 0;
				int maneuver_num = 0;
				int mm = 0;
			  	for(int i = 0;i<path.size()-1;i++)
				{
					Node currentNode = path.get(i);
					Node nextNode = path.get(i+1);
//
					for(Edge e: currentNode.adjacencies){
						if (e.target.value.compareTo(nextNode.value) == 0)
						{
							switch (e.direction){
								case 0:
									maneuverlist[i+start_index] = "straight"; break; //right
								case 1:
									maneuverlist[i+start_index] = "right"; break; //right
								case -1:
									maneuverlist[i+start_index] = "left"; break; //right
								case 2:
									maneuverlist[i+start_index] = "pull_out_left"; break; //right
								case 3:
									maneuverlist[i+start_index] = "pull_out_right"; break; //right
								case 4:
									maneuverlist[i+start_index] = "cross_parking"; break; //right
						}
							break;
						}
					}
				}
			  	maneuver_num = path.size()-1;
				for (int i = 0;i<maneuver_num;i++)
				{
					System.out.println("Maneuver: " + maneuverlist[i]);

				}
				return maneuverlist;
	}


		public static Node[] setGraph(){

		Node n1 = new Node("1");
		Node n2 = new Node("2");
		Node n3 = new Node("3");
		Node n4 = new Node("4");
		Node n5 = new Node("5");
		Node n6 = new Node("6");
		Node n7 = new Node("7");
		Node n8 = new Node("8");
		Node n9 = new Node("9");
		Node n10 = new Node("10");
		Node n11 = new Node("11");

//		Node n14 = new Node("Giurgiu");

				double length_curve_2_out = 137.4/60;
				double length_curve_2_in = 98.2/50;
				double length_curve_3_out = 216/70;
				double length_curve_3_in = 176.7/60;
				double length_s_curve = 190.5/50;
				double length_straight = 50/100;
				double length_stop = 3;


		//initialize the edges clockwise:1, counterclockwise:-1, straight:0, pull_left:2, pull_right:3, park:4
		n1.adjacencies = new Edge[]{
			new Edge(n2,8*length_straight ,3),
			new Edge(n3,16*length_straight ,2)
		};

		n2.adjacencies = new Edge[]{
			new Edge(n1,3*length_straight,4),
			new Edge(n4,3*length_straight,0),
			new Edge(n6,2*length_straight,-1),
			new Edge(n11,length_straight ,1)
		};

		n3.adjacencies = new Edge[]{
			new Edge(n9,3*length_straight, 0),
			new Edge(n10,2*length_straight + length_curve_2_in, -1)
		};  


		n4.adjacencies = new Edge[]{
			new Edge(n7,16*length_straight ,-1),
			new Edge(n8,2*length_straight, 1)
		};

		n5.adjacencies = new Edge[]{
			new Edge(n3,length_straight, 0),
			new Edge(n6,3*length_straight,1),
			new Edge(n11,length_straight ,-1)
		};

		n6.adjacencies = new Edge[]{
			new Edge(n5,length_straight ,1),
			new Edge(n8,3*length_straight ,0),
		};

		n7.adjacencies = new Edge[]{
			new Edge(n3,length_straight ,1),
			new Edge(n4,length_straight ,-1),
            new Edge(n11,length_straight ,0)
		};

		n8.adjacencies = new Edge[]{
			new Edge(n2,length_straight ,0),
			new Edge(n10,length_straight ,1)
		};

            n9.adjacencies = new Edge[]{
                    new Edge(n5,length_straight ,-1),
                    new Edge(n7,length_straight ,0)
            };

            n10.adjacencies = new Edge[]{
                    new Edge(n3,length_straight ,-1),
                    new Edge(n4,length_straight ,1),
                    new Edge(n6,length_straight ,0)
            };

            n11.adjacencies = new Edge[]{
                    new Edge(n2,length_straight ,1),
                    new Edge(n9,length_straight ,-1)
            };



	Node[] nodes = {n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11};

				return nodes;
	}
	public static final String xmlFilePath = "./maneuverlist.xml";

	public static void main(String[] args){

		//initialize the graph base on the Romania map

		int[] hops = new int[args.length+2];
		hops[0] = 1;
		for (int i = 0; i < args.length; i++) {
			hops[i+1] = Integer.parseInt(args[i]);
			System.out.println("hops: " + hops[i+1]);
		}
		hops[args.length+1] = 1;
		int start_node = 1;
		Node[] nodes = setGraph();  // map in the uni

		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			// root element
			Element root = document.createElement("AADC-Maneuver-List");
			document.appendChild(root);
			Attr attrd = document.createAttribute("description");
			attrd.setValue("Teststrecke");
			root.setAttributeNode(attrd);
			// employee element
			Element Sector = document.createElement("AADC-Sector");

			root.appendChild(Sector);

			// set an attribute to staff element
			Attr attr = document.createAttribute("id");
			attr.setValue("0");
			Sector.setAttributeNode(attr);

			String maneuverList[] = new String[20];

			for(int i = 0; i<hops.length;i++) {


				int end_node = hops[i];
				//compute paths
				computePaths(nodes[start_node-1]);

				List<Node> path = getShortestPathTo(nodes[end_node-1]);
				System.out.println("Path: " + path);

//                getManeuverListFinal(path);   // map in the final
				maneuverList = getManeuverList(path);   // map in the uni
				nodes = setGraph();
				start_node = hops[i];
			for (int j=0;j<maneuverList.length;j++){
				Element Maneuver = document.createElement("AADC-Maneuver");

				Sector.appendChild(Maneuver);

				// set an attribute to staff element
				Attr attr1 = document.createAttribute("id");
				attr1.setValue("2");
				Maneuver.setAttributeNode(attr1);

				Attr attr2 = document.createAttribute("action");
				attr2.setValue(maneuverList[j]);
				Maneuver.setAttributeNode(attr2);

				if(maneuverList[j].equals("cross_parking")) {
					Attr attr3 = document.createAttribute("extra");
					attr3.setValue("2");
					Maneuver.setAttributeNode(attr3);
				}

			}
			}
			//you can also use staff.setAttribute("id", "1") for this

/*<?xml version="1.0" encoding="iso-8859-1" standalone="no"?>
<AADC-Maneuver-List description="Teststrecke">
	<AADC-Sector id="0">
		<AADC-Maneuver id="0" action="cross_parking" extra="2"/>
	</AADC-Sector>
</AADC-Maneuver-List>*/


			// create the xml file
			//transform the DOM Object to an XML File
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(xmlFilePath));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			// If you use
			 StreamResult result = new StreamResult(System.out);
			// the output will be pushed to the standard output ...
			// You can use that for debugging

			transformer.transform(domSource, streamResult);

			System.out.println("Done creating XML File");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}


	}


}


//define Node
class Node implements Comparable<Node>{
	
	public final String value;
	public Edge[] adjacencies;
	public double shortestDistance = Double.POSITIVE_INFINITY;
	public Node parent;

	public Node(String val){
		value = val;
	}

	public String toString(){
			return value;
	}

	public int compareTo(Node other){
		return Double.compare(shortestDistance, other.shortestDistance);
	}

}

//define Edge
class Edge{
	public final Node target;
	public final double weight;
		public final int direction;
	public Edge(Node targetNode, double weightVal, int directionVal){
		target = targetNode;
		weight = weightVal;
				direction = directionVal;
	}

}

//define Edge
class Maneuver{
	public final String name;
	public Maneuver(String val){
		name = val;
	}
}