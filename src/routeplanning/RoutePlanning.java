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

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
//import com.optimization.MIP.MIP_Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;

import java.util.logging.Logger;

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


import java.util.*;


public class RoutePlanning {
//    private static final Logger logger = Logger.getLogger(MIP_Test.class.getName());
//
//    /// @brief Print the solution.
//    static void printSolution(
//            RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
//        // Solution cost.
//        logger.info("Objective: " + solution.objectiveValue() + "miles");
//        // Inspect solution.
//        logger.info("Route:");
//        long routeDistance = 0;
//        String route = "";
//        long index = routing.start(0);
//        while (!routing.isEnd(index)) {
//            route += manager.indexToNode(index) + " -> ";
//            long previousIndex = index;
//            index = solution.value(routing.nextVar(index));
//            routeDistance += routing.getArcCostForVehicle(previousIndex, index, 0);
//        }
//        route += manager.indexToNode(routing.end(0));
//        logger.info(route);
//        logger.info("Route distance: " + routeDistance + "miles");
//    }

    /* Dijkstra Algorithm
     *
     *
     */
    public static void computePaths(Node source) {
        source.shortestDistance = 0;

        //implement a priority queue
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        queue.add(source);

        while (!queue.isEmpty()) {
            Node u = queue.poll();

			/*visit the adjacencies, starting from
			the nearest node(smallest shortestDistance)*/

            for (Edge e : u.adjacencies) {

                Node v = e.target;
                double weight = e.weight;

                //relax(u,v,weight)
                double distanceFromU = u.shortestDistance + weight;
                if (distanceFromU < v.shortestDistance) {

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

    public static List<Node> getShortestPathTo(Node target) {

        //trace path from target to source
        List<Node> path = new ArrayList<Node>();
        for (Node node = target; node != null; node = node.parent) {
            path.add(node);
        }


        //reverse the order such that it will be from source to target
        Collections.reverse(path);

        return path;
    }

    public static Integer getDistance(List<Node> path) {
        int path_dist = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node currentNode = path.get(i);
            Node nextNode = path.get(i + 1);
//
            for (Edge e : currentNode.adjacencies) {
                if (e.target.value.compareTo(nextNode.value) == 0) {
                    path_dist = path_dist + e.weight;
                }
            }
        }
        return path_dist;
    }

    public static String[] getManeuverList(List<Node> path) {


        String[] maneuverlist = new String[path.size() - 1];
        int start_index = 0;
        int maneuver_num = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            Node currentNode = path.get(i);
            Node nextNode = path.get(i + 1);
//
            for (Edge e : currentNode.adjacencies) {
                if (e.target.value.compareTo(nextNode.value) == 0) {
//							path_dist=path_dist+e.weight;

                    switch (e.direction) {
                        case 0:
                            maneuverlist[i + start_index] = "straight";
                            break; //right
                        case 1:
                            maneuverlist[i + start_index] = "right";
                            break; //right
                        case -1:
                            maneuverlist[i + start_index] = "left";
                            break; //right
                        case 2:
                            maneuverlist[i + start_index] = "pull_out_left";
                            break; //right
                        case 3:
                            maneuverlist[i + start_index] = "pull_out_right";
                            break; //right
                        case 4:
                            maneuverlist[i + start_index] = "cross_parking";
                            break; //right
                    }
                    break;
                }
            }

        }
//			 	 System.out.println(path_dist);
        maneuver_num = path.size() - 1;

        return maneuverlist;
    }


    public static Node[] setGraph() {

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

        double length_curve_2_out = 137.4 / 60;
        double length_curve_2_in = 2;
        double length_curve_3_out = 216 / 70;
        double length_curve_3_in = 176.7 / 60;
        double length_s_curve = 190.5 / 50;
        int length_straight = 2;
        double length_stop = 3;


        //initialize the edges clockwise:1, counterclockwise:-1, straight:0, pull_left:2, pull_right:3, park:4
        n1.adjacencies = new Edge[]{
                new Edge(n2, 2 * length_straight, 3),
                new Edge(n3, 8 * length_straight, 2)
        };

        n2.adjacencies = new Edge[]{
                new Edge(n1, 8 * length_straight, 4),
                new Edge(n4, 3 * length_straight, 0),
                new Edge(n6, 4 * length_straight, -1),
                new Edge(n11, 3 * length_straight, 1)
        };

        n3.adjacencies = new Edge[]{
                new Edge(n9, 4 * length_straight, 0),
                new Edge(n10, 4 * length_straight, -1)
        };


        n4.adjacencies = new Edge[]{
                new Edge(n7, 4 * length_straight, -1),
                new Edge(n8, 3 * length_straight, 1)
        };

        n5.adjacencies = new Edge[]{
                new Edge(n3, 3 * length_straight, 0),
                new Edge(n6, 4 * length_straight, 1),
                new Edge(n11, 3 * length_straight, -1)
        };

        n6.adjacencies = new Edge[]{
                new Edge(n5, 4 * length_straight, 1),
                new Edge(n8, 4 * length_straight, 0),
        };

        n7.adjacencies = new Edge[]{
                new Edge(n3, 5 * length_straight, 1),
                new Edge(n4, 4 * length_straight, -1),
                new Edge(n11, 4 * length_straight, 0)
        };

        n8.adjacencies = new Edge[]{
                new Edge(n2, 4 * length_straight, 0),
                new Edge(n10, 2 * length_straight, 1)
        };

        n9.adjacencies = new Edge[]{
                new Edge(n5, length_straight, -1),
                new Edge(n7, length_straight, 0)
        };

        n10.adjacencies = new Edge[]{
                new Edge(n3, 3 * length_straight, -1),
                new Edge(n4, 2 * length_straight, 1),
                new Edge(n6, 4 * length_straight, 0)
        };

        n11.adjacencies = new Edge[]{
                new Edge(n2, 4 * length_straight, 1),
                new Edge(n9, 3 * length_straight, -1)
        };


        Node[] nodes = {n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11};

        return nodes;
    }

    public static final String xmlFilePath = "./maneuverlist.xml";

    public static void main(String[] args) {
        final int HOME_ID = 26; // 1
        final int OFFICE_ID = 25; // 7
        final int RESTAURANT_ID = 28; // 2
        final int POST_ID = 27; // 4
        final int SUPERMARKET_ID = 29; // 8
        //initialize the graph base on the Romania map

        int[] hops = new int[args.length + 2];
        hops[0] = 1;
        for (int i = 0; i < args.length; i++) {
            hops[i + 1] = Integer.parseInt(args[i]);
            System.out.println("hops: " + hops[i + 1]);
        }
        hops[args.length + 1] = 1;
//		int start_node = 1;

        long[][] distanceMatrix = new long[args.length + 1][args.length + 1];
        for (int i = 0; i < args.length + 1; i++) {
            Node[] nodes = setGraph();  // map in the uni
            int start_node = hops[i];
            for (int j = 0; j < args.length + 1; j++) {
                int end_node = hops[j];
                //compute paths
                computePaths(nodes[start_node - 1]);

                List<Node> path = getShortestPathTo(nodes[end_node - 1]);
//				System.out.println("Path from " + start_node + " to " + end_node + ":" + path);

//				getManeuverList(path);   // map in the uni
                distanceMatrix[i][j] = getDistance(path);
            }
        }
        for (int i = 0; i < distanceMatrix.length; i++) {
            System.out.println(Arrays.toString(distanceMatrix[i]));
        }

        Loader.loadNativeLibraries();
        // Instantiate the data problem.
//		final RoutePlanning.DataModel data = new RoutePlanning.DataModel();

        // Create Routing Index Manager
        RoutingIndexManager manager =
                new RoutingIndexManager(distanceMatrix.length, 1, 0);

        // Create Routing Model.
        RoutingModel routing = new RoutingModel(manager);

        // Create and register a transit callback.
        final int transitCallbackIndex =
                routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                    // Convert from routing variable Index to user NodeIndex.
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    return distanceMatrix[fromNode][toNode];
                });

        // Define cost of each arc.
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Setting first solution heuristic.
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .build();

        // Solve the problem.
        Assignment solution = routing.solveWithParameters(searchParameters);

        // Print solution on console.
//		printSolution(routing, manager, solution);

        long index = routing.start(0);
//		hops[0] = 1;
        int[] newhops = new int[hops.length];
        int ii = 0;
        while (!routing.isEnd(index)) {
//			System.out.println(manager.indexToNode(index) );
            newhops[ii] = hops[(int) index];
            long previousIndex = index;
            index = solution.value(routing.nextVar(index));
//			routeDistance += routing.getArcCostForVehicle(previousIndex, index, 0);
//			System.out.println(index);
            ii++;
        }
        newhops[ii] = 1;
        System.out.print("New hops: " + newhops[0] + "->");
        for (int i = 1; i < newhops.length - 1; i++)
            System.out.print(newhops[i] + "->");
        System.out.println(newhops[newhops.length - 1]);
//		hops[ii]=1;
//		route += manager.indexToNode(routing.end(0));
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

            Node[] nodes = setGraph();
            int start_node = 1;
            String maneuverList[] = new String[20];
            int maneuverCounter = 0;
            int total_dist = 0;
            for (int i = 1; i < newhops.length; i++) {

                Element Sector = document.createElement("AADC-Sector");

                root.appendChild(Sector);

                // set an attribute to staff element
                Attr attr = document.createAttribute("id");
                attr.setValue(String.valueOf(i-1));
                Sector.setAttributeNode(attr);

                int end_node = newhops[i];
                //compute paths
                computePaths(nodes[start_node - 1]);

                List<Node> path = getShortestPathTo(nodes[end_node - 1]);

                System.out.println("Path: " + path);

//                if(i==newhops.length-1)
//                    String maneuverList[] = new String[path.size()-1];
//                else
//                    String maneuverList[] = new String[path.size()];

                maneuverList = getManeuverList(path);   // map in the uni

//                    maneuverList[path.size()-1] = "car_stop";


                for (int k = 0; k < maneuverList.length; k++) {
                    System.out.println("Maneuver: " + maneuverList[k]);
                }

                nodes = setGraph();
                start_node = newhops[i];
                total_dist = total_dist + getDistance(path);
                for (int j = 0; j < maneuverList.length; j++) {
                    Element Maneuver = document.createElement("AADC-Maneuver");

                    Sector.appendChild(Maneuver);

                    // set an attribute to staff element
                    Attr attr1 = document.createAttribute("id");
                    attr1.setValue(String.valueOf(maneuverCounter));
                    Maneuver.setAttributeNode(attr1);
                    maneuverCounter++;
                    Attr attr2 = document.createAttribute("action");
                    attr2.setValue(maneuverList[j]);
                    Maneuver.setAttributeNode(attr2);
                    if (j == maneuverList.length - 1) {
                        if (maneuverList[j].equals("cross_parking")) {
                            Attr attr3 = document.createAttribute("extra");
                            attr3.setValue("2");
                            Maneuver.setAttributeNode(attr3);
                        } else {
                            Element Maneuver1 = document.createElement("AADC-Maneuver");

                            Sector.appendChild(Maneuver1);

                            // set an attribute to staff element
                            Attr attr4 = document.createAttribute("id");
                            attr4.setValue(String.valueOf(maneuverCounter));
                            Maneuver1.setAttributeNode(attr4);
                            maneuverCounter++;
                            Attr attr5 = document.createAttribute("action");
                            attr5.setValue("car_stop");
                            Maneuver1.setAttributeNode(attr5);
                            Attr attr6 = document.createAttribute("extra");

                            switch(end_node) {
                                case 2:
                                    attr6.setValue("28");
                                    Maneuver1.setAttributeNode(attr6);
                                    break;
                                case 4:
                                    attr6.setValue("27");
                                    Maneuver1.setAttributeNode(attr6);
                                    break;
                                case 7:
                                    attr6.setValue("25");
                                    Maneuver1.setAttributeNode(attr6);
                                    break;
                                case 8:
                                    attr6.setValue("29");
                                    Maneuver1.setAttributeNode(attr6);
                                    break;
                            }

                        }
                    }
                }
            }
            //you can also use staff.setAttribute("id", "1") for this
            System.out.println("Total maneuvers: " + maneuverCounter);
            System.out.println("Total distance: " + total_dist);
//<?xml version="1.0" encoding="iso-8859-1" standalone="no"?>
//<AADC-Maneuver-List description="Teststrecke">
//	<AADC-Sector id="0">
//		<AADC-Maneuver id="0" action="cross_parking" extra="2"/>
//	</AADC-Sector>
//</AADC-Maneuver-List>


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
class Node implements Comparable<Node> {

    public final String value;
    public Edge[] adjacencies;
    public double shortestDistance = Double.POSITIVE_INFINITY;
    public Node parent;

    public Node(String val) {
        value = val;
    }

    public String toString() {
        return value;
    }

    public int compareTo(Node other) {
        return Double.compare(shortestDistance, other.shortestDistance);
    }

}

//define Edge
class Edge {
    public final Node target;
    public final int weight;
    public final int direction;

    public Edge(Node targetNode, int weightVal, int directionVal) {
        target = targetNode;
        weight = weightVal;
        direction = directionVal;
    }

}

//define Edge
class Maneuver {
    public final String name;

    public Maneuver(String val) {
        name = val;
    }
}