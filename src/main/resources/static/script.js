const themeToggle = document.getElementById("themeToggle");
const html = document.documentElement;

if (themeToggle) {
  const savedTheme = localStorage.getItem("theme");
  if (savedTheme) {
    html.setAttribute("data-theme", savedTheme);
    themeToggle.textContent = savedTheme === "dark" ? "☀" : "🌙";
  }

  themeToggle.addEventListener("click", () => {
    const currentTheme = html.getAttribute("data-theme");
    const newTheme = currentTheme === "dark" ? "light" : "dark";
    html.setAttribute("data-theme", newTheme);
    themeToggle.textContent = newTheme === "dark" ? "☀" : "🌙";
    localStorage.setItem("theme", newTheme);
  });
}

const hamburger = document.getElementById("hamburger");
const mobileMenu = document.getElementById("mobileMenu");
const mobileOverlay = document.getElementById("mobileOverlay");

function openMobile() {
  if (mobileMenu && mobileOverlay) {
    mobileMenu.classList.add("active");
    mobileOverlay.classList.add("active");
  }
}

function closeMobile() {
  if (mobileMenu && mobileOverlay) {
    mobileMenu.classList.remove("active");
    mobileOverlay.classList.remove("active");
  }
}

if (hamburger) {
  hamburger.addEventListener("click", openMobile);
}

const navbar = document.getElementById("navbar");
if (navbar) {
  window.addEventListener("scroll", () => {
    if (window.scrollY > 50) {
      navbar.classList.add("scrolled");
    } else {
      navbar.classList.remove("scrolled");
    }
  });
}

const projectsGrid = document.getElementById("projectsGrid");
const tabs = document.querySelectorAll(".tab");
let allProjects = [];

async function fetchProjects() {
  if (!projectsGrid) return;
  try {
    const res = await fetch("/api/projects");
    if (res.ok) {
      allProjects = await res.json();
      renderProjects("all");
    }
  } catch (error) {
    console.error("Failed to fetch projects");
  }
}
function renderProjects(filter) {
  if (!projectsGrid) return;
  projectsGrid.innerHTML = "";

  const filtered =
    filter === "all"
      ? allProjects
      : allProjects.filter((p) => p.category === filter);

  if (filtered.length === 0) {
    projectsGrid.innerHTML =
      "<p style='grid-column: 1/-1; text-align: center; color: #888;'>No projects found.</p>";
    return;
  }

  // 1. ZORBO'YU BUL VE EN ÜSTE YATAY EKLE
  const zorbo = filtered.find((p) => p.title.toLowerCase().includes("zorbo"));
  if (zorbo && (filter === "all" || filter === "team")) {
    projectsGrid.innerHTML += `
        <div class="project-card featured" data-category="${zorbo.category}">
            <div class="featured-header">
                <div style="display: flex; align-items: center; gap: 15px;">
                    <div>
                        <h3 style="margin:0;">${zorbo.title}</h3>
                        <div class="tech-stack" style="margin: 5px 0 0 0;">${zorbo.techStack || ""}</div>
                    </div>
                    ${zorbo.techIcon ? `<img src="https://cdn.simpleicons.org/${zorbo.techIcon}/white" class="tech-logo" style="width: 35px; height: 35px; margin-left: 10px; transform: translateY(-10px);" alt="${zorbo.techIcon}">` : ""}
                </div>
                <a href="${zorbo.githubUrl || "#"}" target="_blank" rel="noopener" class="link-top-right">Steam Link ↗</a>
            </div>
            <p class="desc" style="margin-top: 20px;">${zorbo.description}</p>
            ${zorbo.detail ? `<p class="detail">${zorbo.detail}</p>` : ""}
        </div>
    `;
  }

  // 2. DİĞER PROJELERİ KUTULAR HALİNDE EKLE
  filtered.forEach((p) => {
    if (p.title.toLowerCase().includes("zorbo")) return;

    const card = document.createElement("div");
    card.className = "project-card";

    const badgeClass = p.category === "team" ? "badge-team" : "badge-personal";
    const badgeText = p.category === "team" ? "TEAM" : "PERSONAL";

    let linkHtml = "";
    if (p.githubUrl) {
      linkHtml = `<a href="${p.githubUrl}" target="_blank" rel="noopener" class="project-link-btn">Link</a>`;
    }

    card.innerHTML = `
          <div class="project-card-header">
              <div>
                  <span class="project-badge ${badgeClass}">${badgeText}</span>
                  <h3>${p.title}</h3>
              </div>
              ${p.techIcon ? `<img src="https://cdn.simpleicons.org/${p.techIcon}/white" class="tech-logo" style="width: 30px; height: 30px;" alt="${p.techIcon}">` : ""}
          </div>
          <div class="tech-stack">${p.techStack || ""}</div>
          <p class="desc">${p.description}</p>
          ${p.detail ? `<p class="detail">${p.detail}</p>` : ""}
          <div class="project-links">
              ${linkHtml}
          </div>
      `;
    projectsGrid.appendChild(card);
  });
}

if (tabs.length > 0) {
  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      tabs.forEach((t) => t.classList.remove("active"));
      tab.classList.add("active");
      renderProjects(tab.dataset.filter);
    });
  });
  fetchProjects();
}

const contactForm = document.getElementById("contactForm");

if (contactForm) {
  const statusMsg = document.createElement("p");
  statusMsg.style.marginTop = "10px";
  statusMsg.style.fontSize = "0.9rem";
  statusMsg.style.fontWeight = "500";
  contactForm.appendChild(statusMsg);

  contactForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const name = contactForm
      .querySelector('input[placeholder="Your name"]')
      .value.trim();
    const email = contactForm
      .querySelector('input[placeholder="your@email.com"]')
      .value.trim();
    const message = contactForm.querySelector("textarea").value.trim();
    const btn = contactForm.querySelector("button");

    statusMsg.textContent = "";

    if (!name || !email || !message) {
      statusMsg.style.color = "#ff4d4d";
      statusMsg.textContent = "Please fill in all fields.";
      return;
    }

    if (name.length <= 3) {
      statusMsg.style.color = "#ff4d4d";
      statusMsg.textContent = "Name must be longer than 3 characters.";
      return;
    }

    if (!email.includes("@")) {
      statusMsg.style.color = "#ff4d4d";
      statusMsg.textContent =
        "Please enter a valid email address containing '@'.";
      return;
    }

    if (message.length <= 30) {
      statusMsg.style.color = "#ff4d4d";
      statusMsg.textContent = "Message must be longer than 30 characters.";
      return;
    }

    const formData = { name, email, message, subject: "Portfolio Contact" };

    btn.textContent = "Sending...";
    btn.disabled = true;

    try {
      const response = await fetch("/api/contact", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        statusMsg.style.color = "#4BB543";
        statusMsg.textContent = `Message sent successfully, ${name}!`;
        contactForm.reset();
      } else if (response.status === 429) {
        statusMsg.style.color = "#ffcc00";
        statusMsg.textContent =
          "Too many requests. Please try again in 1 hour.";
      } else {
        statusMsg.style.color = "#ff4d4d";
        statusMsg.textContent = "An error occurred. Please try again later.";
      }
    } catch (error) {
      statusMsg.style.color = "#ff4d4d";
      statusMsg.textContent = "Could not reach the server.";
    } finally {
      btn.textContent = "Send Message";
      btn.disabled = false;
      setTimeout(() => {
        statusMsg.textContent = "";
      }, 5000);
    }
  });
}
