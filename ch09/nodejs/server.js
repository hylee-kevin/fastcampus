const express = require('express');
const app = express();
const path = require('path');

// Serve static files from the "public" directory
app.use(express.static('public'));

// Serve the "images" directory as a static folder
app.use('/images', express.static('public/images'));


// Route for the homepage
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public/index.html'));
});

// Start the server
const port = 3000;
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});

