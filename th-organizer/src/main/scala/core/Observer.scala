package core

trait Observer[T] {

    def receiveUpdate(update: T)

}
