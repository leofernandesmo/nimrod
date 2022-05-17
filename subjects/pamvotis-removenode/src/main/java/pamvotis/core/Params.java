package pamvotis.core;

/**
 * Contains the parameters of a node, like coverage, position, data rate e.t.c
 * @author Dimitris El. Vassis
 */
public class Params {
	
	int id; //The ID if the node
	int rate;	//The data rate (bits/s)
	int x;	//The node's horizontal (x) coordinate.
	int y;	//The node's vertical (y) coordinate.
	int coverage;	//The node's coverage.
	int ac=0;	//The node's access category (802.11e)
	float aifsd= 0;	//The difs parameter for 802.11e
	int cwMin=0;	//The minimum contention window (depending on the access category)
	int cwMax=0;	//The maximum contention window (depending on the access category)
	
	/**
	 * Initializes the parameters of a node.
	 * @param id The ID of the node
	 * @param rate The data rate of a node. Users should be careful when setting the data rate, as it must
	 * conform the data rates that the physical layer supports. Pamvotis supports data rate agility, meaning
	 * that the data rate can change according to the distance from another node, but users must pay attention
	 * to the data rates that can be supported by each physical layer.
	 * @param x The x-axis position coordinate in meters.
	 * @param y The y-axis position coordinate in meters.
	 * @param coverage The node's coverage in meters.
	 * @param ac The 802.11e access category 0,1,2,3
	 * @param aifsd The aifsd parameter, according to the access category
	 * @param cwMin The minimum contention window
	 * @param cwMax The maximum contention window
	 */
	public void InitParams(int id, int rate,int x, int y, int coverage, int ac, float aifsd, int cwMin, int cwMax)
	{
		this.id = id;
		this.rate = rate;
		this.x = x;
		this.y = y;
		this.coverage = coverage;
		this.ac = ac;
		this.aifsd = aifsd;
		this.cwMin = cwMin;
		this.cwMax = cwMax;
	}

	
	/**
	 * Calculates the distance of the specific node from another node.
	 * @param fromx The x-distance from the other node, e.g. specificNode.x-anotnerNode.x
	 * @param fromy The y-distance from the other node, e.g. specificNode.y-anotnerNode.y
	 * @return The distance from the other node in meters.
	 */
	public int DistFrom(int fromx, int fromy)
	{
		double xdist = (double) fromx-x;
		double ydist = (double) fromy-y;
		return (int) Math.sqrt(Math.pow(xdist,2)+Math.pow(ydist,2));
	}
}
