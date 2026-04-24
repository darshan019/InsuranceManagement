import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Inline SVG icons — no external deps, consistent stroke style
const IconMotor = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="32" height="32">
    <path d="M5 17h-1a1 1 0 0 1-1-1v-3l2-5a2 2 0 0 1 2-1h10a2 2 0 0 1 2 1l2 5v3a1 1 0 0 1-1 1h-1" />
    <circle cx="7" cy="17" r="2" />
    <circle cx="17" cy="17" r="2" />
    <path d="M9 17h6" />
    <path d="M5 13h14" />
  </svg>
);

const IconLife = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="32" height="32">
    <path d="M12 21s-7-4.5-7-10a4 4 0 0 1 7-2.65A4 4 0 0 1 19 11c0 5.5-7 10-7 10z" />
  </svg>
);

const IconProperty = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="32" height="32">
    <path d="M3 10l9-7 9 7" />
    <path d="M5 9v11a1 1 0 0 0 1 1h4v-6h4v6h4a1 1 0 0 0 1-1V9" />
  </svg>
);

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

      <section className="hero">
        <div className="hero-inner">
          <h1>Insurance, made simple.</h1>
          <p className="hero-sub">
            Protect what matters most. Explore motor, life, and property
            insurance plans from trusted agents &mdash; all in one place.
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

      <section id="offerings" className="section">
        <div className="section-inner">
          <h2>What we offer</h2>
          <p className="section-lead">
            Three categories of coverage to protect every important area of your life.
          </p>

          <div className="offering-grid">
            <div className="offering-card">
              <div className="offering-icon-wrap"><IconMotor /></div>
              <h3>Motor Insurance</h3>
              <p>
                Comprehensive cover for cars, two-wheelers, and commercial
                vehicles. Third-party liability, accident protection, and more.
              </p>
            </div>

            <div className="offering-card">
              <div className="offering-icon-wrap"><IconLife /></div>
              <h3>Life Insurance</h3>
              <p>
                Term and whole-life plans that secure your family's future.
                Flexible premiums, strong coverage, and reliable payouts.
              </p>
            </div>

            <div className="offering-card">
              <div className="offering-icon-wrap"><IconProperty /></div>
              <h3>Property Insurance</h3>
              <p>
                Protect your home, office, or rented property against fire,
                theft, and natural disasters with tailored plans.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section id="why" className="section section-alt">
        <div className="section-inner">
          <h2>Why choose us</h2>

          <div className="feature-grid">
            <div className="feature-item">
              <div className="feature-num">01</div>
              <h4>Transparent Plans</h4>
              <p>Clear premiums, clear coverage. No fine-print surprises.</p>
            </div>
            <div className="feature-item">
              <div className="feature-num">02</div>
              <h4>Fast Claims</h4>
              <p>Submit a claim online and track its status in real time.</p>
            </div>
            <div className="feature-item">
              <div className="feature-num">03</div>
              <h4>Trusted Agents</h4>
              <p>Work with certified agents who help you pick the right plan.</p>
            </div>
            <div className="feature-item">
              <div className="feature-num">04</div>
              <h4>Secure Payments</h4>
              <p>Pay premiums safely via UPI, cards, or net banking.</p>
            </div>
          </div>
        </div>
      </section>

      {!user && (
        <section className="cta-strip">
          <div className="section-inner cta-inner">
            <div>
              <h3>Ready to get covered?</h3>
              <p>Create a customer account and browse plans in minutes.</p>
            </div>
            <Link to="/register" className="btn-solid btn-lg">Create Account</Link>
          </div>
        </section>
      )}

      <footer id="contact" className="public-footer">
        <div className="section-inner footer-grid">
          <div>
            <div className="brand">
              <span className="brand-mark">IM</span>
              <span className="brand-name">Insurance Management</span>
            </div>
            <p className="footer-muted">
              A simple insurance management platform for customers, agents,
              and administrators.
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
              <li>Mon &ndash; Sat, 9am &ndash; 6pm</li>
            </ul>
          </div>
        </div>
        <div className="footer-bottom">
          <span>&copy; {new Date().getFullYear()} Insurance Management. All rights reserved.</span>
        </div>
      </footer>
    </div>
  );
}
