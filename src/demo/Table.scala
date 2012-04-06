package demo

trait Table {

  def insert(record: Record)
  
  def foreach(f: Record=>Unit )
  
}