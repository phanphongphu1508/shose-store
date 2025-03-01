import React, { useState } from 'react'; // Thêm import useState
import 'jquery';
import 'bootstrap/dist/js/bootstrap.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import Background from '../../assets/images/login-bg1.jpg';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import './signup.css';

function Signup() {
    const hostname = "http://localhost:8080";
    let history = useHistory();
    const [isAgreed, setIsAgreed] = useState(false); // State để theo dõi trạng thái checkbox

    const linkgg = 'https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/api/auth/login-google&response_type=code&client_id=721465338797-4lcqo4qql4vmevh90pnaahvo6tem4lsr.apps.googleusercontent.com&approval_prompt=force';
    const linkfb = 'https://www.facebook.com/dialog/oauth?client_id=1101972766995519&redirect_uri=http://localhost:8080/api/auth/login-facebook';

    const validateEmail = (email) => {
        const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    };

    const signupAccount = () => {
        debugger;
        var usernames = document.getElementById("register-email").value;
        var passwords = document.getElementById("register-password").value;
        var firstnames = document.getElementById("register-firstname").value;
        var lastnames = document.getElementById("register-lastname").value;
        var phones = document.getElementById("register-phone").value;
        var addresss = document.getElementById("register-address").value;

        // Kiểm tra xem người dùng đã đồng ý với chính sách bảo mật chưa
        if (!isAgreed) {
            alert("Vui lòng đồng ý với chính sách bảo mật trước khi đăng ký.");
            return false;
        }

        if (usernames.length < 3 || firstnames.length < 3 || lastnames.length < 3) {
            alert("Dữ liệu không hợp lệ");
            return false;
        }

        if (!validateEmail(usernames)) {
            alert("Email không hợp lệ");
            return false;
        }

        if (passwords.length < 6) {
            alert("Mật khẩu phải từ 6 ký tự");
            return false;
        }

        if (addresss.length < 6) {
            alert("Địa chỉ phải từ 6 ký tự");
            return false;
        }

        if (phones.length != 10) {
            alert("Số điện thoại không hợp lệ");
            return false;
        }

        const bodyParameters = {
            email: usernames,
            password: passwords,
            firstname: firstnames,
            lastname: lastnames,
            phone: phones,
            address: addresss,
            role: ["USER"],
        };

        console.log(JSON.stringify(bodyParameters));
        axios
            .post(hostname + '/api/auth/signup', bodyParameters)
            .then((Response) => {
                alert("Đăng ký thành công");
                console.log("Signup success");
                history.push("/login");
            })
            .catch((error) => {
                console.log("Error " + error.response);
                alert(error.message);
            });
    };

    return (
        <>
            <main className="main">
                <nav aria-label="breadcrumb" className="breadcrumb-nav border-0 mb-0">
                    <div className="container">
                        <ol className="breadcrumb">
                            <li className="breadcrumb-item"><Link to="index.html">Home</Link></li>
                            <li className="breadcrumb-item active" href="" aria-current="page">Login</li>
                        </ol>
                    </div>
                </nav>

                <div className="login-page bg-image pt-8 pb-8 pt-md-12 pb-md-12 pt-lg-17 pb-lg-17" style={{ backgroundImage: "url(" + Background + ")" }} >
                    <div className="container">
                        <div className="form-box">
                            <div className="form-tab">
                                <ul className="nav nav-pills nav-fill" role="tablist">
                                    <li className="nav-item">
                                        <Link className="nav-link" id="signin-tab-2" data-toggle="tab" to="/login" role="tab" aria-controls="signin-2" aria-selected="false">Sign In</Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link active" id="register-tab-2" data-toggle="tab" href="#register-2" role="tab" aria-controls="register-2" aria-selected="true">Register</Link>
                                    </li>
                                </ul>
                                <div className="tab-content">
                                    <div className="tab-pane fade show active" id="register-2" role="tabpanel" aria-labelledby="register-tab-2">
                                        <div>
                                            <form method='post' action="" onSubmit='return false'>
                                                <div className="form-group">
                                                    <label htmlFor="register-email-2">Your email address *</label>
                                                    <input style={{ fontSize: 14 }} type="email" className="form-control" id="register-email" name="register-email" required />
                                                </div>
                                                <div className="row">
                                                    <div className="col-sm-6">
                                                        <label htmlFor="register-password-2">FirstName *</label>
                                                        <input style={{ fontSize: 14 }} type="text" minLength="3" className="form-control" id="register-firstname" name="register-firstname" required />
                                                    </div>
                                                    <div className="col-sm-6">
                                                        <label htmlFor="register-password-2">LastName *</label>
                                                        <input style={{ fontSize: 14 }} minLength="3" type="text" className="form-control" id="register-lastname" name="register-lastname" required />
                                                    </div>
                                                </div>
                                                <div className="form-group">
                                                    <label htmlFor="register-password-2">Password *</label>
                                                    <input style={{ fontSize: 14 }} minLength="4" type="password" className="form-control" id="register-password" name="register-password" required />
                                                </div>
                                                <div className="form-group">
                                                    <label htmlFor="register-password-2">Phone *</label>
                                                    <input style={{ fontSize: 14 }} type="tel" maxLength="10" minLength="10" className="form-control" id="register-phone" name="register-phone" pattern="[0-9]{10}" required />
                                                </div>
                                                <div className="form-group">
                                                    <label htmlFor="register-password-2">Address *</label>
                                                    <input style={{ fontSize: 14 }} minLength="5" type="text" className="form-control" id="register-address" name="register-address" required />
                                                </div>

                                                <div className="form-footer">
                                                    <button
                                                        type="button"
                                                        className="signup-button"
                                                        onClick={signupAccount}
                                                    >
                                                        <span>SIGN UP</span>
                                                    </button>
                                                    <div className="custom-control custom-checkbox">
                                                        <input
                                                            type="checkbox"
                                                            className="custom-control-input"
                                                            id="register-policy-2"
                                                            required
                                                            onChange={(e) => setIsAgreed(e.target.checked)} // Cập nhật state khi checkbox thay đổi
                                                        />
                                                        <label className="custom-control-label" htmlFor="register-policy-2">
                                                            I agree to the <a href="/privacy">privacy policy</a> *
                                                        </label>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
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

export default Signup;