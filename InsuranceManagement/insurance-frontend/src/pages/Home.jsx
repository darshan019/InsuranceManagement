import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FaCar, FaHeart, FaHome } from 'react-icons/fa';

export default function Home() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const goToDashboard = () => {
    if (!user) return;
    if (user.role === 'ADMIN') navigate('/admin');
    else if (user.role === 'AGENT') navigate('/agent');
    else navigate('/customer');
  };

  return (
    <div className="home-page">
      {/* ---------- Header ---------- */}
      <header className="public-header">
        <div className="public-header-inner">
          <div className="brand">
            <span className="brand-mark">IM</span>
            <span className="brand-name">Insurance Management</span>
          </div>

          <nav className="public-nav">
            <a href="#offerings">Plans</a>
            <a href="#why">Why us</a>
            <a href="#contact">Contact</a>

            {user ? (
              <button className="btn-outline" onClick={goToDashboard}>
                Go to Dashboard
              </button>
            ) : (
              <>
                <Link to="/login" className="btn-outline">Login</Link>
                <Link to="/register" className="btn-solid">Register</Link>
              </>
            )}
          </nav>
        </div>
      </header>

      {/* ---------- Hero ---------- */}
      <section className="hero">
        <div className="hero-inner">
          <h1>Insurance, made simple.</h1>
          <p className="hero-sub">
            Protect what matters most. Explore motor, life, and property
            insurance plans from trusted agents — all in one place.
          </p>

          <div className="hero-actions">
            {user ? (
              <button className="btn-solid btn-lg" onClick={goToDashboard}>
                Go to Dashboard
              </button>
            ) : (
              <>
                <Link to="/register" className="btn-solid btn-lg">Get Started</Link>
                <Link to="/login" className="btn-outline btn-lg">Sign In</Link>
              </>
            )}
          </div>
        </div>
      </section>

      {/* ---------- Offerings ---------- */}
      <section id="offerings" className="section">
        <div className="section-inner">
          <h2>What we offer</h2>
          <p className="section-lead">
            Three categories of coverage to protect every important area of your life.
          </p>

          <div className="offering-grid">
            <div className="offering-card">
              <div className="offering-icon-wrap">
                <FaCar size={32} />
              </div>
              <h3>Motor Insurance</h3>
              <p>
                Comprehensive cover for cars, two-wheelers, and commercial
                vehicles. Third-party liability and accident protection.
              </p>
            </div>

            <div className="offering-card">
              <div className="offering-icon-wrap">
                <FaHeart size={32} />
              </div>
              <h3>Life Insurance</h3>
              <p>
                Term and whole-life plans that secure your family’s future
                with flexible premiums and reliable payouts.
              </p>
            </div>

            <div className="offering-card">
              <div className="offering-icon-wrap">
                <FaHome size={32} />
              </div>
              <h3>Property Insurance</h3>
              <p>
                Protect your home or office against fire, theft, and natural
                disasters with tailored coverage.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* ---------- Why Us ---------- */}
      <section id="why" className="section section-alt">
        <div className="section-inner">
          <h2>Why choose us</h2>

          <div className="feature-grid">
            <div className="feature-item">
              <div className="feature-num">01</div>
              <h4>Transparent Plans</h4>
              <p>Clear premiums and coverage with no hidden clauses.</p>
            </div>

            <div className="feature-item">
              <div className="feature-num">02</div>
              <h4>Fast Claims</h4>
              <p>File claims online and track status in real time.</p>
            </div>

            <div className="feature-item">
              <div className="feature-num">03</div>
              <h4>Trusted Agents</h4>
              <p>Certified agents to guide you at every step.</p>
            </div>

            <div className="feature-item">
              <div className="feature-num">04</div>
              <h4>Secure Payments</h4>
              <p>Safe payments via cards, net banking, or UPI.</p>
            </div>
          </div>
        </div>
      </section>

      {/* ---------- CTA ---------- */}
      {!user && (
        <section className="cta-strip">
          <div className="section-inner cta-inner">
            <div>
              <h3>Ready to get covered?</h3>
              <p>Create a customer account and browse plans in minutes.</p>
            </div>
            <Link to="/register" className="btn-solid btn-lg">
              Create Account
            </Link>
          </div>
        </section>
      )}

      {/* ---------- Footer ---------- */}
      <footer id="contact" className="public-footer">
        <div className="section-inner footer-grid">
          <div>
            <div className="brand">
              <span className="brand-mark">IM</span>
              <span className="brand-name">Insurance Management</span>
            </div>
            <p className="footer-muted">
              A simple insurance management platform for customers,
              agents, and administrators.
            </p>
          </div>

          <div>
            <h5>Plans</h5>
            <ul>
              <li>Motor</li>
              <li>Life</li>
              <li>Property</li>
            </ul>
          </div>

          <div>
            <h5>Company</h5>
            <ul>
              <li>About</li>
              <li>Careers</li>
              <li>Partners</li>
            </ul>
          </div>

          <div>
            <h5>Contact</h5>
            <ul>
              <li>support@insurance.example</li>
              <li>+91 1800-000-000</li>
              <li>Mon – Sat, 9am – 6pm</li>
            </ul>
          </div>
        </div>

        <div className="footer-bottom">
          <span>
            © {new Date().getFullYear()} Insurance Management. All rights reserved.
          </span>
        </div>
      </footer>
    </div>
  );
}
