import {SiMaildotcom} from "react-icons/si";
import '../Pages/nav-bar.css'
import PropTypes from "prop-types";
import {FiLogOut} from "react-icons/fi";
import {CgProfile} from "react-icons/cg";
import {useNavigate} from "react-router-dom";

NavBar.propTypes = {
    user : PropTypes.string,
}

function NavBar(props) {
    const navigate = useNavigate();
    return (
        <div className="NavBar">
            <div className={"logo"}>
                <SiMaildotcom style={{fontSize: "3.3rem", color: "red"}}/>
                <h1>My Mail</h1>
            </div>

            <div className={"nav-seg"}>
                <div className={"nav"}>
                    <CgProfile style={{margin: "10px"}}/>
                    <p>{props.user}</p>
                </div>
                <div className={"nav"} onClick={() =>navigate("/")}>
                    <FiLogOut style={{margin: "10px"}}/>
                    <p>Log Out</p>
                </div>

            </div>

        </div>
    )
}

export default NavBar;