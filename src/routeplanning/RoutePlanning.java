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

		public static void getManeuverListFinal(List<Node> path){


				String[] maneuverlist = new String[20];
				int start_index = 0;
				int maneuver_num = 0;
				double temp_d = 0;
				double D1 = 0;
				double D2 = 0;
				if (path.get(0).value.compareTo("2") == 0 &&
					 path.get(1).value.compareTo("3") == 0 )
				{
					maneuverlist[0] = "Pull_out_left";
					start_index = 1;
					System.out.println("start park 2");
//                    System.out.println("Maneuver: " + maneuverlist[0]);
				}
				else if (path.get(0).value.compareTo("2") == 0 &&
						 path.get(1).value.compareTo("10") == 0 )
				{
					maneuverlist[0] = "Pull_out_right";
					start_index = 1;
					System.out.println("start park 2");
//                    System.out.println("Maneuver: " + maneuverlist[0]);
				}

				for(int i = 0;i<path.size()-2;i++)
				{
					Node currentNode = path.get(i);
					Node nextNode = path.get(i+1);
					Node nextnextNode = path.get(i+2);

					for(Edge e: currentNode.adjacencies){
						if (e.target.value.compareTo(nextNode.value) == 0)
						{
							D1 = e.direction;
							break;
						}
			
					}

					for(Edge e: nextnextNode.adjacencies){
						if (e.target.value.compareTo(nextNode.value) == 0)
						{
							D2 = e.direction;
							break;
						}
			
					}
						if (D2 == 0 )
							temp_d = 180;
						else if (D2 == 180 )
							temp_d = 0;
						else
							temp_d = -D2;
//                        
						if (D1 - temp_d == 90 || D1 - temp_d == -270)
							maneuverlist[i+start_index] = "right"; //right
						else if (D1 - temp_d == -90 || D1 - temp_d == 270 )
							maneuverlist[i+start_index] = "left";
						else
							maneuverlist[i+start_index] = "straight"; //straight
//                        
//                    System.out.println("Maneuver: " + maneuverlist[i+start_index]);
				}
				if (path.get(path.size()-1).value.compareTo("2") == 0 )
				{
					maneuverlist[path.size()-2+start_index] = "cross_parking";
					maneuver_num = path.size()-2+start_index + 1;
				}
				else
					maneuver_num = path.size()-2+start_index;

				for (int i = 0;i<maneuver_num;i++)
				{
					System.out.println("Maneuver: " + maneuverlist[i]);

				}

	}

		public static Node[] setGraphFinal(){

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
		Node n12 = new Node("12");
		Node n13 = new Node("13");
//		Node n14 = new Node("Giurgiu");

				double length_curve_2_out = 137.4/60;
				double length_curve_2_in = 98.2/50;
				double length_curve_3_out = 216/70;
				double length_curve_3_in = 176.7/60;
				double length_s_curve = 190.5/50;
				double length_straight = 50/100;
				double length_stop = 3;


		//initialize the edges
		n1.adjacencies = new Edge[]{
			new Edge(n3,2*length_straight + length_stop,-90),
		};

		n2.adjacencies = new Edge[]{
			new Edge(n3,7*length_straight,90),
			new Edge(n10,4*length_straight + length_stop,-90)
		};

		n3.adjacencies = new Edge[]{
			new Edge(n1,2*length_straight, 90),
			new Edge(n2,7*length_straight + 999, -90),
						new Edge(n4,length_straight, 0),
						new Edge(n10,13*length_straight + length_stop+999, -90)
		};  


		n4.adjacencies = new Edge[]{
			new Edge(n3,length_straight + length_stop,180),
			new Edge(n5,length_straight, 0),
			new Edge(n6,4*length_straight,-90)
		};

		n5.adjacencies = new Edge[]{
			new Edge(n4,length_straight, 180),
			new Edge(n7,4*length_straight,-90),
						new Edge(n8,4*length_straight + length_curve_2_in,-90),
						new Edge(n11,length_straight,-90),
			new Edge(n12,2*length_straight + length_curve_2_in,-90)
		};

		n6.adjacencies = new Edge[]{
			new Edge(n4,4*length_straight + length_stop,90),
			new Edge(n7,length_straight + length_stop,0),
			new Edge(n9,5*length_straight + length_curve_2_out,0)
		};

		n7.adjacencies = new Edge[]{
			new Edge(n5,4*length_straight + length_stop,90),
			new Edge(n6,length_straight + length_stop,180),
			new Edge(n8,2*length_straight + length_stop,0),
						new Edge(n11,length_straight,90)
				};

		n8.adjacencies = new Edge[]{
			new Edge(n5,4*length_straight + length_curve_2_out,180),
						new Edge(n7,2*length_straight + length_stop,180),
			new Edge(n9,3*length_straight,-90),                        
			new Edge(n12,length_straight,90),                        
			new Edge(n13,length_straight,-90)

		};

		n9.adjacencies = new Edge[]{
						new Edge(n6,5*length_straight+length_curve_2_in,90),
						new Edge(n8,2*length_straight,90),
			new Edge(n10,3*length_straight + length_curve_2_in + length_s_curve,180),                        
			new Edge(n13,length_straight,90)
		};



		n10.adjacencies = new Edge[]{
						new Edge(n2,5*length_straight + 999,90),
					new Edge(n3,13*length_straight + length_stop,90),
						new Edge(n9,3*length_straight + length_curve_2_out + length_s_curve,90)
		};    

				n11.adjacencies = new Edge[]{
					new Edge(n5,length_straight ,90),
						new Edge(n7,length_straight ,-90)
		};  

				n12.adjacencies = new Edge[]{
					new Edge(n5,length_straight + length_curve_2_out,180),
						new Edge(n8,length_straight,-90)
		};  

			n13.adjacencies = new Edge[]{
					new Edge(n8,length_straight,90),
						new Edge(n9,length_straight,-90)
				};

		Node[] nodes = {n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13};

				return nodes;
	}
 

		  public static String[] getManeuverList(List<Node> path){


				String[] maneuverlist = new String[20];
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
//				double temp_d = 0;
//				double D1 = 0;
//				double D2 = 0;
//				if (path.get(0).value.compareTo("1") == 0 &&
//					 path.get(1).value.compareTo("4") == 0 )
//				{
//					maneuverlist[0] = "Pull_out_left";
//					start_index = 1;
//					System.out.println("start park 2");
////                    System.out.println("Maneuver: " + maneuverlist[0]);
//				}
//				else if (path.get(0).value.compareTo("1") == 0 &&
//						 path.get(1).value.compareTo("2") == 0 )
//				{
//					maneuverlist[0] = "Pull_out_right";
//					start_index = 1;
//					System.out.println("start park 2");
////                    System.out.println("Maneuver: " + maneuverlist[0]);
//				}
//
//				for(int i = 0;i<path.size()-2;i++)
//				{
//					Node currentNode = path.get(i);
//					Node nextNode = path.get(i+1);
//					Node nextnextNode = path.get(i+2);
//
//					for(Edge e: currentNode.adjacencies){
//						if (e.target.value.compareTo(nextNode.value) == 0)
//						{
//							D1 = e.direction;
//							break;
//						}
//
//					}
//
//					for(Edge e: nextnextNode.adjacencies){
//						if (e.target.value.compareTo(nextNode.value) == 0)
//						{
//							D2 = e.direction;
//							break;
//						}
//
//					}
//						if (D2 == 0 )
//							temp_d = 180;
//						else if (D2 == 180 )
//							temp_d = 0;
//						else
//							temp_d = -D2;
////
//						if (D1 - temp_d == 90 || D1 - temp_d == -270)
//							maneuverlist[i+start_index] = "right"; //right
//						else if (D1 - temp_d == -90 || D1 - temp_d == 270 )
//							maneuverlist[i+start_index] = "left";
//						else
//							maneuverlist[i+start_index] = "straight"; //straight
////
////                    System.out.println("Maneuver: " + maneuverlist[i+start_index]);
//				}
//				if (path.get(path.size()-1).value.compareTo("1") == 0 )
//				{
//					maneuverlist[path.size()-2+start_index] = "cross_parking";
//					maneuver_num = path.size()-2+start_index + 1;
//				}
//				else
//					maneuver_num = path.size()-2+start_index;

//				for (int i = 0;i<maneuver_num;i++)
//				{
//					System.out.println("Maneuver: " + maneuverlist[i]);
//
//				}

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
	public static final String xmlFilePath = "C:\\Users\\xuhu7477\\IdeaProjects\\Routeplanning\\src\\xmlfile.xml";

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
//		Node[] nodes = setGraphFinal();   // map in the final
		Node[] nodes = setGraph();  // map in the uni


//		Node[] nodes = setGraph();
//		int start_node = 1;
//		int end_node = 6;
//		//compute paths
//
//		computePaths(nodes[start_node-1]);
//
//		List<Node> path = getShortestPathTo(nodes[end_node-1]);
//		System.out.println("Path: " + path);
//
////                getManeuverListFinal(path);   // map in the final
//		getManeuverList(path);   // map in the uni
//
//		nodes = setGraph();
//		start_node = 6;
//		end_node = 7;
//		//compute paths
//
//		computePaths(nodes[start_node-1]);
//
//		path = getShortestPathTo(nodes[end_node-1]);
//		System.out.println("Path: " + path);
//
////                getManeuverListFinal(path);   // map in the final
//		getManeuverList(path);   // map in the uni
		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			// root element
			Element root = document.createElement("AADC-Maneuver-List");
			document.appendChild(root);

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
			}
			}
			//you can also use staff.setAttribute("id", "1") for this



			// create the xml file
			//transform the DOM Object to an XML File
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(xmlFilePath));

			// If you use
			// StreamResult result = new StreamResult(System.out);
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