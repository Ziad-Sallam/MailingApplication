import PropTypes from "prop-types";


ErrorMsg.propTypes = {
    message: PropTypes.string.isRequired
};

function ErrorMsg(props) {
    return(
        <p style={{color: "red", fontWeight: "light"}} >
            {props.message}
        </p>
    )
}
export default  ErrorMsg