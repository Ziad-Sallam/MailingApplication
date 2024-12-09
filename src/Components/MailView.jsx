import PropTypes from "prop-types";

const mail = {
    sender : PropTypes.string,
    receiver : PropTypes.arrayOf(PropTypes.string),
    body : PropTypes.string,
    subject: PropTypes.string,
    date : PropTypes.string,
    id : PropTypes.number,
}

MailView.propTypes = {
    mail : PropTypes.oneOfType(mail)

}
function MailView(props) {
    return (
        <div className="mail-view">
                <table className={"table table-striped"}>
                    <tbody>
                    <tr>
                        <td><label htmlFor="sender">From: </label></td>
                        <td>{props.mail.sender}
                        </td>
                    </tr>
                    <tr>
                        <td>To: </td>
                        <td>You
                        </td>
                    </tr>
                    <tr>
                        <td>Subject: </td>
                        <td>{props.mail.subject}
                        </td>
                    </tr>

                    </tbody>

                </table>
            <div className={"mail-body"}>
                {props.mail.body}
            </div>

        </div>
    )
}

export default MailView;