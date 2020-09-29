package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.client.Client;
import seedu.address.model.session.Session;

/**
 * Represents the in-memory model of the FitEgo's data (client + schedule).
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final ScheduleList scheduleList;
    private final UserPrefs userPrefs;
    private final FilteredList<Client> filteredClients;
    private final FilteredList<Session> filteredSessions;

    /**
     * Initializes a ModelManager with the given addressBook, scheduleList and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyScheduleList scheduleList,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.scheduleList = new ScheduleList(scheduleList);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredClients = new FilteredList<>(this.addressBook.getClientList());
        filteredSessions = new FilteredList<>(this.scheduleList.getScheduleList());
    }

    public ModelManager() {
        this(new AddressBook(), new ScheduleList(), new UserPrefs());
    }

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        //TODO: TO REMOVE THIS METHOD.
        //NEED TO REFACTOR TEST CASES TO REMOVE THIS
        this(addressBook, new ScheduleList(), userPrefs);
        logger.warning("THIS SHOULDNT BE HERE");
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public Path getScheduleListFilePath() {
        return userPrefs.getSessionListFilePath();
    }

    @Override
    public void setScheduleListFilePath(Path scheduleListFilePath) {
        requireNonNull(scheduleListFilePath);
        userPrefs.setSessionListFilePath(scheduleListFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return addressBook.hasClient(client);
    }

    @Override
    public void deleteClient(Client target) {
        addressBook.removeClient(target);
    }

    @Override
    public void addClient(Client client) {
        addressBook.addClient(client);
        updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Override
    public void setClient(Client target, Client editedClient) {
        requireAllNonNull(target, editedClient);

        addressBook.setClient(target, editedClient);
    }

    //=========== Filtered Client List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Client} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Client> getFilteredClientList() {
        return filteredClients;
    }

    @Override
    public void updateFilteredClientList(Predicate<Client> predicate) {
        requireNonNull(predicate);
        filteredClients.setPredicate(predicate);
    }

    //=========== ScheduleList ================================================================================

    @Override
    public void setScheduleList(ReadOnlyScheduleList scheduleList) {
        this.scheduleList.resetData(scheduleList);
    }

    @Override
    public ReadOnlyScheduleList getScheduleList() {
        return scheduleList;
    }

    @Override
    public boolean hasSession(Session session) {
        requireNonNull(session);
        return scheduleList.hasSession(session);
    }

    @Override
    public void deleteSession(Session session) {
        scheduleList.removeSession(session);
    }

    @Override
    public void addSession(Session session) {
        scheduleList.addSession(session);
        updateFilteredSessionList(PREDICATE_SHOW_ALL_SESSIONS);
    }

    @Override
    public void setSession(Session target, Session editedSession) {
        requireAllNonNull(target, editedSession);
        scheduleList.setSession(target, editedSession);
    }

    //=========== Filtered Client List Accessors =============================================================

    @Override
    public ObservableList<Session> getFilteredSessionList() {
        return filteredSessions;
    }

    @Override
    public void updateFilteredSessionList(Predicate<Session> predicate) {
        requireNonNull(predicate);
        filteredSessions.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredClients.equals(other.filteredClients);
    }

}
