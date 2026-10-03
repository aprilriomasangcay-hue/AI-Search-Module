package com.mycompany.ai;

import java.util.*;

class NodeCost {
    String node;
    int cost;

    public NodeCost(String node, int cost) {
        this.node = node;
        this.cost = cost;
    }
}

class AStarNode implements Comparable<AStarNode> {
    String node;
    int gCost;
    int fCost;
    List<String> path;

    public AStarNode(String node, int gCost, int fCost, List<String> path) {
        this.node = node;
        this.gCost = gCost;
        this.fCost = fCost;
        this.path = new ArrayList<>(path);
        this.path.add(node);
    }

    @Override
    public int compareTo(AStarNode other) {
        return Integer.compare(this.fCost, other.fCost);
    }
}

public class AI {

    private static final Map<String, List<NodeCost>> graph = new HashMap<>();

    private static final Map<String, Integer> heuristics = new HashMap<>();

    static {
        graph.put("S", Arrays.asList(new NodeCost("A", 3), new NodeCost("B", 4)));
        graph.put("A", Arrays.asList(new NodeCost("C", 5)));
        graph.put("B", Arrays.asList(new NodeCost("D", 3), new NodeCost("E", 2)));
        graph.put("C", Arrays.asList(new NodeCost("G", 6)));
        graph.put("D", Arrays.asList(new NodeCost("G", 1)));
        graph.put("E", Arrays.asList(new NodeCost("G", 8)));
        graph.put("G", Collections.emptyList());

        heuristics.put("S", 7);
        heuristics.put("A", 6);
        heuristics.put("B", 4);
        heuristics.put("C", 5);
        heuristics.put("D", 1);
        heuristics.put("E", 7);
        heuristics.put("G", 0);
    }

    public static List<String> bfs(String start, String goal) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(Collections.singletonList(start));

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String node = path.get(path.size() - 1);

            if (node.equals(goal)) {
                return path;
            }

            if (!visited.contains(node)) {
                visited.add(node);
                for (NodeCost neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor.node);
                    queue.add(newPath);
                }
            }
        }
        return null;
    }

    public static List<String> dfs(String start, String goal) {
        Stack<List<String>> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        stack.push(Collections.singletonList(start));

        while (!stack.isEmpty()) {
            List<String> path = stack.pop();
            String node = path.get(path.size() - 1);

            if (node.equals(goal)) {
                return path;
            }

            if (!visited.contains(node)) {
                visited.add(node);
                for (NodeCost neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor.node);
                    stack.push(newPath);
                }
            }
        }
        return null;
    }

    public static void aStarSearch(String start, String goal) {
        PriorityQueue<AStarNode> pq = new PriorityQueue<>();
        Map<String, Integer> visitedCosts = new HashMap<>();

        int initialH = heuristics.getOrDefault(start, 0);
        pq.add(new AStarNode(start, 0, initialH, new ArrayList<>()));

        while (!pq.isEmpty()) {
            AStarNode current = pq.poll();

            if (current.node.equals(goal)) {
                System.out.println("A* Path: " + current.path + " with Total Cost: " + current.gCost);
                return;
            }

            if (visitedCosts.containsKey(current.node) && visitedCosts.get(current.node) <= current.gCost) {
                continue;
            }

            visitedCosts.put(current.node, current.gCost);

            for (NodeCost neighbor : graph.getOrDefault(current.node, Collections.emptyList())) {
                int newGCost = current.gCost + neighbor.cost;
                int newFCost = newGCost + heuristics.getOrDefault(neighbor.node, 0);
                pq.add(new AStarNode(neighbor.node, newGCost, newFCost, current.path));
            }
        }
        System.out.println("No path found using A* Search.");
    }

    public static void main(String[] args) {
        System.out.println("BFS Path: " + bfs("S", "G"));
        System.out.println("DFS Path: " + dfs("S", "G"));
        aStarSearch("S", "G");
    }
}