import PropTypes from "prop-types";

const mail = {
    sender : PropTypes.string,
    receiver : PropTypes.arrayOf(PropTypes.string),
    body : PropTypes.string,
    subject: PropTypes.string,
    date : PropTypes.string,
    id : PropTypes.number,
}

ListBox.propTypes = {
    mails: PropTypes.arrayOf(PropTypes.shape(mail))
}

function ListBox() {
    const mails = []
    return (
        <div className="list-box">
            <table className="table table-striped list-box-table table-hover">
                <thead>
                <tr>
                    <th>
                        From
                    </th>
                    <th>
                        Subject
                    </th>
                    <th>
                        Date
                    </th>
                </tr>
                </thead>
                <tbody>

                {mails.map((mail, index) => (
                    <tr key={index}>
                        <td>{mail.sender}</td>
                        <td>{mail.subject}</td>
                        <td>{mail.date}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default ListBox;