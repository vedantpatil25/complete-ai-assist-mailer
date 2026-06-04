/* ---------------- POPUP ---------------- */

function createPopup(onConfirm) {
  const overlay = document.createElement("div");
  overlay.className = "ai-overlay";

  const modal = document.createElement("div");
  modal.className = "ai-modal";

  modal.innerHTML = `
    <div class="ai-title">AI Reply Options</div>

    <label class="ai-label">User Recommended Response</label>
    <textarea id="ai-user-input" rows="4" class="ai-textarea"></textarea>

    <label class="ai-label">Tone</label>
    <select id="ai-tone" class="ai-select">
      <option value="professional">Professional</option>
      <option value="friendly">Friendly</option>
      <option value="formal">Formal</option>
      <option value="casual">Casual</option>
    </select>

    <div class="ai-buttons">
      <button id="ai-cancel" class="ai-btn ai-cancel">Cancel</button>
      <button id="ai-confirm" class="ai-btn ai-confirm">Generate</button>
    </div>
  `;

  overlay.appendChild(modal);
  document.body.appendChild(overlay);

  document.getElementById("ai-cancel").onclick = () => {
    overlay.remove();
  };

  document.getElementById("ai-confirm").onclick = () => {
    const userText = document.getElementById("ai-user-input").value;
    const tone = document.getElementById("ai-tone").value;

    overlay.remove();
    onConfirm(userText, tone);
  };
}

/* ---------------- FIND TOOLBAR ---------------- */

const findComposeToolbar = () => {
  const selectors = [".btC", ".gU.Up", '[role="dialog"] .btC'];

  for (const selector of selectors) {
    const toolbar = document.querySelector(selector);
    if (toolbar) return toolbar;
  }

  return null;
};

/* ---------------- CREATE BUTTON ---------------- */

const createAIButton = () => {
  const button = document.createElement("div");

  button.className = "T-I J-J5-Ji aoO v7 T-I-atl L3 ai-reply-button";
  button.style.marginRight = "8px";
  button.style.cursor = "pointer";

  button.innerText = "AI Reply";

  button.setAttribute("role", "button");
  button.setAttribute("data-tooltip", "Generate AI Reply");

  return button;
};

/* ---------------- GET EMAIL CONTENT ---------------- */

const getEmailContent = () => {
  const selectors = [
    ".h7",
    ".a3s.aiL",
    ".gmail_quote",
    '[role="presentation"]',
  ];

  for (const selector of selectors) {
    const content = document.querySelector(selector);
    if (content) {
      return content.innerText.trim();
    }
  }

  return "";
};

/* ---------------- INJECT BUTTON ---------------- */

const injectButton = () => {
  if (document.querySelector(".ai-reply-button")) return;

  const toolbar = findComposeToolbar();

  if (!toolbar) {
    return;
  }

  const button = createAIButton();

  button.addEventListener("mousedown", () => {
    createPopup(async (userText, tone) => {
      try {
        button.innerText = "Generating...";
        button.style.pointerEvents = "none";

        const emailContent = getEmailContent();

        const response = await fetch(`${CONFIG.API_BASE_URL}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            emailContent: emailContent,
            userRecommendation: userText,
            tone: tone,
          }),
        });

        if (!response.ok) {
          throw new Error("API request failed");
        }

        const data = await response.json();
        const generatedReply = data.response;

        const composeBox = document.querySelector(
          '[role="textbox"][g_editable="true"], div[aria-label="Message Body"]',
        );

        if (composeBox) {
          composeBox.focus();

          document.execCommand("insertText", false, generatedReply);
        }
      } catch (error) {
        console.error("Error generating reply:", error);
      } finally {
        button.innerText = "AI Reply";
        button.style.pointerEvents = "auto";
      }
    });
  });

  toolbar.insertBefore(button, toolbar.firstChild);
};

/* ---------------- OBSERVER ---------------- */

const observer = new MutationObserver((mutations) => {
  let hasComposeElements = false;

  for (const mutation of mutations) {
    const addedNodes = Array.from(mutation.addedNodes);

    const found = addedNodes.some(
      (node) =>
        node.nodeType === Node.ELEMENT_NODE &&
        (node.matches(".aDh, .btC, [role='dialog']") ||
          node.querySelector(".aDh, .btC, [role='dialog']")),
    );

    if (found) {
      hasComposeElements = true;
      break;
    }
  }

  if (hasComposeElements) {
    setTimeout(injectButton, 500);
  }
});

/* ---------------- START OBSERVER ---------------- */

observer.observe(document.body, {
  childList: true,
  subtree: true,
});
