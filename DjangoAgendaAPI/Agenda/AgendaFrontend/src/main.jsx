import { StrictMode } from 'react'; // Enables additional checks and warnings in development
import { createRoot } from 'react-dom/client'; // React 18 API for creating root rendering
import './index.css'; // Global CSS styles
import App from './App.jsx'; // Main App component

// Create root and render React app into the HTML element with id "root"
createRoot(document.getElementById('root')).render(
  <StrictMode>
    {/* Wrap App in StrictMode to help detect potential issues */}
    <App />
  </StrictMode>,
);