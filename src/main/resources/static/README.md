# RinneSoft Portfolio — Setup Guide

## File Structure
```
rinnesoft/
├── index.html        — Main portfolio page
├── admin.html        — Admin panel (password protected)
├── style.css         — All styles
├── script.js         — All JavaScript
├── images/
│   └── zorbo/        — Put Zorbo screenshots here (or use admin panel)
└── README.md         — This file
```

## Deploying

### Option A — GitHub Pages (free, recommended)
1. Create a repo at github.com (e.g. `rinnesoft.github.io` or `rinnesoft-portfolio`)
2. Upload all files
3. Go to Settings → Pages → Deploy from branch → main
4. Done — your site will be live at `yourusername.github.io/repo-name`

### Option B — Your own server (Nginx)
1. Copy all files to `/var/www/rinnesoft.com/`
2. Point your Nginx config to that folder
3. Add SSL with Certbot

---

## Admin Panel

URL: `yoursite.com/admin.html`  
Default password: `rinnesoft2025`

**To change the password:**  
Open `admin.html`, find this line near the top of the `<script>` block:
```js
const ADMIN_PW = 'rinnesoft2025';
```
Change it to whatever you want.

### What you can do in Admin:
- **Projects** — Add new projects (title, description, tech stack, GitHub link). They appear on the portfolio automatically.
- **Skills** — Add new skill groups with comma-separated items.
- **Zorbo Images** — Upload screenshots directly from your browser. They appear in the Zorbo project gallery on the main page.
- **About / Bio** — Override the bio text for Furkan and Alper.
- **Contact Info** — Update emails, GitHub, and LinkedIn links.

All data is saved in your **browser's localStorage**, so:
- It persists between visits on the same browser
- If you move to a new browser/device, you'll need to re-add content
- For a permanent solution on a real server, you'd replace localStorage with a small backend (ask for a server-side version if needed)

---

## Adding Zorbo Screenshots

**Via Admin Panel (recommended):**
1. Go to `admin.html`
2. Login
3. Go to the "Zorbo Images" tab
4. Upload your screenshots

**Manually:**
1. Put images in `images/zorbo/` folder named `zorbo1.jpg`, `zorbo2.jpg`, etc.
2. In `script.js`, the gallery loads from localStorage — for file-based loading, just add `<img>` tags directly into the `#zorboGallery` div in `index.html`

---

## Customising

All real content is in `index.html` — search for what you want to change:
- University, departments, bios → About section
- Project descriptions → Projects section
- Emails, GitHub URLs, LinkedIn → Contact section
- Skills/tech stack → Skills section
