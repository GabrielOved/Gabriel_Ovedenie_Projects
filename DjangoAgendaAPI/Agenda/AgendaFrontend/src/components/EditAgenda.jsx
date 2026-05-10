import { useState, useRef, useEffect } from "react"; // React hooks for state, refs, lifecycle
import "./EditAgenda.css"; // Import CSS styles

export default function EditAgenda() { // Main component
    const API_URL = "http://localhost:8000/api/Agenda/"; // Backend API endpoint

    const [title, setTitle] = useState(""); // Title input value
    const [dateTime, setDateTime] = useState(""); // Date-time input value
    const [type, setType] = useState("Reminder"); // Selected agenda type

    const [search, setSearch] = useState(""); // Search input value
    const [deleteMode, setDeleteMode] = useState(false); // Toggle delete mode
    const [selectedDelete, setSelectedDelete] = useState([]); // IDs selected for deletion

    const [agendaData, setAgendaData] = useState([]); // List of agenda items from API


    const [filterOpen, setFilterOpen] = useState(false); // Open/close filter dropdown
    const [selectedDate, setSelectedDate] = useState(""); // Selected date filter
    const [selectedType, setSelectedType] = useState(""); // Selected type filter

    const availableDates = [...new Set(agendaData.map(item => item.date))]; // Unique dates
    const availableTypes = [...new Set(agendaData.map(item => item.type))]; // Unique types

    const filterRef = useRef(null); // Reference to filter dropdown for outside click detection

    // FETCH DATA ON MOUNT
    useEffect(() => {
          fetch(API_URL) // Call API
            .then(response => response.json()) // Convert response to JSON
            .then(data => {
              const normalized = data.map(item => ({ ...item, id: Number(item.id) })); // Ensure ID is numeric
              setAgendaData(normalized); // Store data in state
            })
            .catch(err => console.error(err)); // Error handling
        }, []);

    // CLOSE FILTER WHEN CLICKING OUTSIDE
    useEffect(() => {
      const handleClickOutside = (event) => {
        if (filterRef.current && !filterRef.current.contains(event.target)) {
          setFilterOpen(false); // Close dropdown
        }
      };
      document.addEventListener("mousedown", handleClickOutside); // Listen for clicks
      return () => {
        document.removeEventListener("mousedown", handleClickOutside); // Cleanup listener
      };
    }, []);

    // ADD NEW AGENDA ITEM
    const handleAdd = async () => {
          if (!title || !dateTime) return; // Validate input

          const newAgenda = { title, date_time: dateTime, type };

          try {
            const response = await fetch(API_URL, {
              method: "POST", // Create request
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(newAgenda), // Send data
            });

            const saved = await response.json(); // Get response

            const normalized = { ...saved, id: Number(saved.id) }; // Normalize ID

            setAgendaData(prev => [...prev, normalized]); // Add new item
            setTitle(""); // Reset title
          } catch (err) {
            console.error(err); //Error handling
          }
        };

    // DELETE SELECTED ITEMS    
    const handleDelete = async () => {
          if (!deleteMode) {
            setDeleteMode(true); // Enable delete mode first click
            return;
          }

          if (selectedDelete.length === 0){
            setDeleteMode(false); // Exit if nothing selected
            return;
          } 

          try {
            await Promise.all(
              selectedDelete.map((id) =>
                fetch(`${API_URL}${id}/`, { method: "DELETE" }) // Delete each item
              )
            );

            setAgendaData(prev => prev.filter(item => !selectedDelete.includes(item.id))); // Remove from UI

            setSelectedDelete([]); // Clear selection
            setDeleteMode(false); // Exit delete mode

          } catch (err) {
            console.error(err); // Error handling
          }
        };

    return (
      <div className="container"> {/* Main wrapper */}
        
        <h1 className="page-title" style={{ marginBottom: "28px" }}>
          Agenda API
        </h1>
        
        {/* INPUT SECTION */}
        <div className="section top-section">

          <input
            id="Title"
            placeholder="Title"
            type="text"
            value={title}
            onChange={(event) => setTitle(event.target.value)} // Update title
          />

          {/* DATE INPUT */}
          <div className="form-row">
            <div className="form-label">Date & Time:</div>
            <input
              type="datetime-local"
              value={dateTime}
              onChange={(event) => setDateTime(event.target.value)} // Update date
            />
          </div>

          {/* TYPE SELECT */}
          <div className="form-row">
            <div className="form-label">Type:</div>
            <select id="Type" value={type} onChange={(event) => setType(event.target.value)}> // Update type
              <option value="Release">Release</option>
              <option value="Reminder">Reminder</option>
              <option value="Event">Event</option>
            </select>
          </div>

          <button className="add-btn" onClick={handleAdd}>
            Add
          </button>

        </div>

        {/* SEARCH + FILTER */}
        <div className="section search-section">

          <input
            id='Search'
            type="text"
            placeholder="Search..."
            value={search}
            onChange={(event) => setSearch(event.target.value)} // Update search
          />

          {/* FILTER WRAPPER */}
          <div className="filter" ref={filterRef}>
            <button onClick={(event) => {
                    event.stopPropagation(); // Prevent outside click handler
                    setFilterOpen(prev => !prev); // Toggle filter
                  }}>
              Filter
            </button>

            {/* FILTER DROPDOWN */}     
            {filterOpen && (
              <div className="filter-dropdown">

                {/* DATE FILTER COLUMN */}
                <div className="filter-column">
                  <div className="title-of-filter">Date</div>
                  {/* Loop through unique available dates */}
                  {availableDates.map((date) => {

                    // Disable logic: checks if this date exists in data
                    const disabled = !agendaData.some(item => item.type === type);

                    return (
                      <div
                        key={date}
                        className={`filter-option ${
                          selectedDate === date ? "active" : ""
                        } ${disabled ? "disabled" : ""}`}

                        // Toggle date filter when clicked
                        onClick={() =>
                          !disabled &&
                          setSelectedDate((prev) => (prev === date ? "" : date))
                        }
                      >
                        {date}
                      </div>
                    );
                  })}
                </div>

                {/* TYPE FILTER COLUMN */}
                <div className="filter-column">
                  <div className="title-of-filter">Type</div>

                  {/* Static list of available types */}
                  {["Release", "Reminder", "Event"].map((type) => {

                    // Disable logic for type filtering
                    const disabled = !agendaData.some(item => item.type === type);

                    return (
                      <div
                        key={type}
                        className={`filter-option ${
                          selectedType === type ? "active" : ""
                        } ${disabled ? "disabled" : ""}`}

                        // Toggle type filter when clicked
                        onClick={() =>
                          !disabled &&
                          setSelectedType((prev) => (prev === type ? "" : type))
                        }
                      >
                        {type}
                      </div>
                    );
                  })}
                </div>

              </div>
            )}
          </div>

        </div>


        {/* DELETE SECTION */}
        <div className="section delete-section">

          {/* Delete button toggles delete mode or executes deletion */}
          <button
            className={`delete-btn ${deleteMode ? "active" : ""}`}
            onClick={handleDelete}
          >
            Delete
          </button>

        </div>


        {/* AGENDA LIST */}
        <div className="list">

          {/* Filter agenda items based on search + filters */}
          {agendaData
            .filter(item => {

                // Normalize search input
                const query = search.toLowerCase();

                // Check if item matches search text
                const matchesSearch =
                  item.title.toLowerCase().includes(query) ||
                  item.date.toLowerCase().includes(query) ||
                  item.type.toLowerCase().includes(query);

                // Check date filter (if selected)
                const matchesDate = selectedDate ? item.date === selectedDate : true;
                // Check type filter (if selected)
                const matchesType = selectedType ? item.type === selectedType : true;

                return matchesSearch && matchesDate && matchesType;
              })
            .map((agenda) => { // Render filtered agenda items
                    return (
                      <div key={agenda.id} className="card">
                        {/* Show checkbox only in delete mode */}
                        {deleteMode && (
                          <input
                              type="checkbox"
                              checked={selectedDelete.includes(agenda.id)}

                              // Toggle selection for deletion
                              onChange={() => {
                                setSelectedDelete(prev =>
                                  prev.includes(agenda.id)
                                    ? prev.filter(i => i !== agenda.id)
                                    : [...prev, agenda.id]
                                );
                              }}
                            />
                        )}

                    {/* Agenda item content */}
                    <div className="card-content">
                      {/* Title Frontend Output*/}
                      <div className="Agenda-title">{agenda.title}</div>
                      {/* Date formatting (replace T and Z for readability) */}
                      <div className="Agenda-datetime">📅: {agenda.date_time.replace("T", ", ").replace("Z", "")}</div>
                      {/* Type Frontend Output*/}
                      <div className="Agenda-type">Type: {agenda.type}</div>
                    </div>
                  </div>
                );
              })}
          </div>

    </div>
  );
}