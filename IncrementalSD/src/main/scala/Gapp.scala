import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

object Gapp {
  def main(args : Array[String]) {
    // Create SparkContext -- boilerplate
    val conf = new SparkConf().setAppName("graphMod")
    val sc = new SparkContext(conf)

    /* Read graph data from input file, and create raw RDD.
     * Input graph format: one edge per line, where each line is a 
     * string "srcId dstId weight".
     * 'file' is set to a MappedRDD object.
     */
    val file = sc.textFile("/user/akshay/tree1024.gr")


    //  Map 'file' to EdgeRDD, by parsing records appropriately.
    val edrdd = file.map{ed =>
      val comps = ed.split(" ")
      new Edge(comps(0).toInt, comps(1).toInt, comps(2).toDouble) }

//      edrdd.saveAsTextFile("/user/debug/outa1")

    // Create graph from EdgeRDD
    var gr = Graph.fromEdges(edrdd, 0)

    /* -- Testing 'updateEdge'. Should really write unit test for this.
     * TODO: Check spark unit test moduel.
     */

    /*val gmod = new graphMod()
    val newEd = "2 0 555"
    val ngr = gmod.updateEdge(newEd, gr) */
    //debug
    //ngr.edges.collect().foreach(println)
    val gmod = new graphMod()
    gmod.run(gr).vertices.map{ case (vid,vattr) => (vid,vattr.distSoFar)}.saveAsTextFile("/user/akshay/mod_result")

    //gr = gmod.updateEdge("0 2 1", gr)
    //gmod.run(gr)

  }
}
