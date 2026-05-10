import axios from "axios"; // Import Axios for HTTP requests

// Create Axios instance with Agenda Backend URL
const API = axios.create({
  baseURL: "http://127.0.0.1:8000", // Backend Agenda server URL
});

export default API; // Export configured instance for reuse across app