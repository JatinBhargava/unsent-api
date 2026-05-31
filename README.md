# 📝 Open Diary Platform

A modern, privacy-first writing platform that helps people express themselves, connect with others, and build meaningful long-form content together.

This project is focused on **human stories**, **thoughtful collaboration**, and **responsible use of AI** to support creativity — not replace it.

---

## 🌱 Why This Project Exists

Writing is deeply personal, yet growth often comes from shared experiences.

Most platforms today are optimized for:
- Short-form content
- Algorithmic virality
- Passive consumption

This project explores a different direction:
- Long-form writing
- Intentional interaction
- Creative continuity
- Community-driven evolution of ideas

We’re building a space where writing can **start privately**, **grow socially**, and **scale sustainably**.


---

## 🎯 Project Goals

- Enable people to write freely and safely
- Support thoughtful engagement over shallow reactions
- Encourage collaboration without losing authorship
- Build creator-friendly systems that respect ownership
- Use AI as an assistant, not a replacement

---

## 🧩 What This Repository Covers

This repository contains the **core platform infrastructure**, including:

- User identity and access control
- Long-form content creation and management
- Social interaction primitives (comments, reactions, follows)
- Content versioning and history
- Moderation and safety foundations
- Scalable backend architecture
- AI integration points (implementation abstracted)
Some advanced product capabilities are **intentionally abstracted** or gated behind interfaces.
- [Frontend Project](https://github.com/JatinBhargava/unsent-ui/tree/develop)

---

## 🔒 What This Repository Does *Not* Expose

To protect the long-term direction of the product, this repo does **not** include:

- Proprietary ranking or recommendation logic
- Growth mechanics or discovery algorithms
- Monetization strategies
- Advanced AI prompting or personalization logic

Contributors don’t need access to these to make meaningful impact.

---

## 🛠️ Tech Stack (Current)

- **Frontend:** React.js
- **Backend:**  Spring Boot
- **Database:** PostgreSQL
- **Cache / Queue:** Redis
- **AI Layer:** Abstracted service interface
- **Infra:** Docker, CI/CD ready

The stack may evolve — architectural discussions are welcome.

---

## 🤝 How You Can Contribute

We welcome contributions in:

- Backend APIs
- Database modeling
- Performance improvements
- Security & privacy
- Moderation tooling
- Testing & reliability
- Documentation
- Developer experience

You don’t need to understand the full product vision to contribute — we value **clean engineering and thoughtful design**.

---

## 🧠 Contribution Principles

- Respect user privacy by default
- Favor clarity over cleverness
- Design for scale, but build pragmatically
- Write code that someone else can maintain
- Ask questions — discussion is encouraged

---

## 📌 Stories Board

All user stories, sprint planning, and PI-level tracking live here:

🔗 **Stories (Notion Board)**  
**Backlog & Sprint Tracking:**  
[Stories Board](https://deadpan-vegetarian-956.notion.site/3007b497118f80ce88fdde290298d016?v=efeabdc05f9d49ab858b5f586762d843)

Use this board to:
- View sprint-wise user stories
- Track PI progress
- Update status, priority, and ownership
- Groom and refine backlog items

---

## 🚀 Getting Started

```bash
# Clone the repository
git clone https://github.com/JatinBhargava/unsent-core.git

# Start services
docker compose up -d 