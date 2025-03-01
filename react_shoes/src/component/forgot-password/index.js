import "./style1.css";
import "jquery";
import "bootstrap/dist/js/bootstrap.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Background from "../../assets/images/login-bg1.jpg";
import { Link } from "react-router-dom";
import axios from "axios";
import { useEffect } from "react";
import { showError, showMessage } from "../message/index";

export default function Forgot() {
  const hostname = "http://localhost:8080";
  const resetPassword = () => {
    const email = document.getElementById("reset-email").value;

    axios
      .get(hostname + `/api/auth/forgot?email=${email}`)
      .then((response) => {
        document.getElementById("responseMessage").innerText =
          "Check your email for the reset link.";
        document.getElementById("responseMessage").style.color = "green";
      })
      .catch((error) => {
        document.getElementById("responseMessage").innerText =
          "Error: " + error.response.data.message;
        document.getElementById("responseMessage").style.color = "red";
      });
  };

  return (
    <>
      <main className="main">
        <nav aria-label="breadcrumb" className="breadcrumb-nav border-0 mb-0">
          <div className="container">
            <ol className="breadcrumb">
              <li className="breadcrumb-item">
                <Link to="/">Home</Link>
              </li>
              <li
                className="breadcrumb-item active"
                href=""
                aria-current="page"
              >
                Login
              </li>
            </ol>
          </div>
        </nav>

        <div
          className="login-page bg-image pt-8 pb-8 pt-md-12 pb-md-12 pt-lg-17 pb-lg-17"
          style={{ backgroundImage: "url(" + Background + ")" }}
        >
          <div className="container">
            <div className="form-box">
              <div className="form-tab">
                <div className="tab-content">
                  <div
                    className="tab-pane fade show active"
                    id="signin-2"
                    role="tabpanel"
                    aria-labelledby="signin-tab-2"
                  >
                    <form action="">
                      <div className="form-group">
                        <label for="reset-email">Email address</label>
                        <input
                          style={{ fontSize: 14 }}
                          type="email"
                          className="form-control"
                          id="reset-email"
                          name="reset-email"
                          required
                        />
                      </div>

                      <p id="responseMessage"></p>

                      <div className="form-footer">
                        <button
                          type="button"
                          onClick={resetPassword}
                          className="btn btn-outline-primary-2"
                        >
                          <span>Reset Password</span>
                          <i className="icon-long-arrow-right"></i>
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </>
  );
}
