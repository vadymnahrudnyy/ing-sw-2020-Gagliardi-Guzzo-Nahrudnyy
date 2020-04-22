package Server;
public interface SantoriniObservable {

    /**
     * SantoriniObservable is an interface that implements the Observable pattern
     */

    /**
     * this method add an observer to the internal list of observers
     * @param obs is the observer that will be added
     */
    public void attachObserver(SantoriniObservable obs);

    /**
     * this method delete an observer to the internal list of observers
     * @param obs is the observer that will be deleted
     */
    public void detachObserver(SantoriniObserver obs);

    /**
     *this method sets the internal flag that indicates this observable has changed state
     */
    public void setChangedStatus();

    /**
     * this method checks the internal flag to see if the observable has changed state and notifies all observers
     */
    public void notifyAllObservers();

    /**
     * this method checks the internal flag to see if the observable has changed state, notifies all observers
     * and passes the object specified in the parameter list to the notify() method of the observer
     * @param obs obs is the observer that will be notified
     */
    public void notifyObserver(SantoriniObserver obs);


    /**
     * this method deletes all observers from the internal list of observers
     */
    public void deleteAllObservers();

    /**
     * @return 1 if the status has changed, 0 is the status has remained the same
     */
    public boolean hasChanged();

}
