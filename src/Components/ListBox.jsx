import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";

const mail = {
    sender: PropTypes.string,
    receiver: PropTypes.arrayOf(PropTypes.string),
    body: PropTypes.string,
    subject: PropTypes.string,
    date: PropTypes.string,
    id: PropTypes.number,
    priority: PropTypes.number // Include priority in PropTypes
};

ListBox.propTypes = {
    mails: PropTypes.arrayOf(PropTypes.shape(mail))
};

function ListBox() {
    const navigate = useNavigate();
    const params = useParams();

    const [mails, setMails] = useState([
        {
            sender: "aa",
            receiver: ["sccs"],
            body: "hello",
            subject: "zz",
            date: "22/10/2024",
            id: 2,
            priority: 0
        },
        {
            sender: "zz",
            receiver: ["merer"],
            body: "zello World",
            subject: "aa",
            date: "23/10/2024",
            id: 3,
            priority: 1
        }
    ]);

    useEffect(() => {
        async function fetchMails() {
            const param = {
                email: params.user,
                folder: "Inbox"
            };
            console.log(param);

            try {
                const data = await axios.get("http://localhost:8080/api/users/getEmails", { params: param });
                console.log(data.data);
                setMails(data.data);
            } catch (error) {
                console.error('Error fetching emails:', error);
            }
        }
        fetchMails();
    }, [params.user]);

    function sort(e) {
        let sorted;
        console.log(e.target.value);
        switch(e.target.value) {
            case "importance":
                sorted = [...mails].sort((a, b) => b.priority - a.priority);
                setMails(sorted);
                break;
            case "body":
                sorted = [...mails].sort((a, b) => a.body.toLowerCase().localeCompare(b.body.toLowerCase()));
                setMails(sorted);
                break;
            case "date":
                sorted = [...mails].sort((a, b) => b.id - a.id);
                setMails(sorted);
                break;
            case "subject":
                sorted = [...mails].sort((a, b) => a.subject.toLowerCase().localeCompare(b.subject.toLowerCase()));
                setMails(sorted);
                break;
            case "from":
                sorted = [...mails].sort((a, b) => a.sender.toLowerCase().localeCompare(b.sender.toLowerCase()));
                setMails(sorted);
                break;
            default:
                break;
        }
    }

    return (
        <div className="list-box">
            <label>{"Sort by:  "}</label>
            <select id="sort-dropdown" onChange={sort}>
                <option value="date">Date</option>
                <option value="subject">Subject</option>
                <option value="body">Body</option>
                <option value="importance">Importance</option>
                <option value="from">From</option>
            </select>
            <table className="table table-striped list-box-table table-hover">
                <thead>
                <tr>
                    <th>From</th>
                    <th>Subject</th>
                    <th>Date</th>
                    <th>Priority</th>
                </tr>
                </thead>
                <tbody>
                {mails.map((mail, index) => (
                    <tr key={index} onClick={() => { navigate("/" + params.user + "/" + mail.id) }}>
                        <td>{mail.sender}</td>
                        <td>{mail.subject}</td>
                        <td>{mail.dateSent}</td>
                        <td>{mail.priority}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default ListBox;
