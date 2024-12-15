import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";


function ContactBox() {
    const navigate = useNavigate();
    const params = useParams();

    const [contact, setContacts] = useState([
        {
            name: "aa",
            userEmail: "aa@example.com"
        },

        {
            sender: "zz",
            userEmail: "zz@example.com"
        }
    ]);

    // useEffect(() => {

    //     async function fetchMails() {
    //         const param = {
    //             email: params.user,
    //             folder: params.folderName
    //         };
    //         console.log(param);

    //         try {
    //            
    //                 const data = await axios.get("http://localhost:8080/api/users/getDrafts", { params: param });
    //                 console.log(data.data);
    //                 setMails(data.data);
    //                 mails.map(mail => {
    //                     mail.id = mail.temp
    //                 })
    //                 console.log(mails);

    //             
    //             

    //         } catch (error) {
    //             console.error('Error fetching emails:', error);
    //         }
    //     }
        
    //     fetchMails();
    // }, [params.folderName, params.user]);

    // function sort(e) {
    //     console.log(mails)
    //     let sorted;
    //     console.log(e.target.value);
    //     switch(e.target.value) {
    //         case "importance":
    //             sorted = [...mails].sort((a, b) => b.priority - a.priority);
    //             setMails(sorted);
    //             break;
    //         case "body":
    //             sorted = [...mails].sort((a, b) => a.body.toLowerCase().localeCompare(b.body.toLowerCase()));
    //             setMails(sorted);
    //             break;
    //         case "date":
    //             sorted = [...mails].sort((a, b) => b.id - a.id);
    //             setMails(sorted);
    //             break;
    //         case "subject":
    //             sorted = [...mails].sort((a, b) => a.subject.toLowerCase().localeCompare(b.subject.toLowerCase()));
    //             setMails(sorted);
    //             break;
    //         case "from":
    //             sorted = [...mails].sort((a, b) => a.sender.toLowerCase().localeCompare(b.sender.toLowerCase()));
    //             setMails(sorted);
    //             break;
    //         default:
    //             break;
    //     }
    // }

    return (
        <div className="Contact-box">
            {/* <label>{"Sort by:  "}</label>
            <select id="sort-dropdown" onChange={sort}>
                <option value="date">Date</option>
                <option value="subject">Subject</option>
                <option value="body">Body</option>
                <option value="importance">Importance</option>
                <option value="from">From</option>
            </select> */}
            <table className="table table-striped list-box-table table-hover">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                {contact.map((name, userEmail) => (
                    // <tr key={userEmail} onClick={() => {
                    //     navigate("/" + params.user + "/" + (mail.id ===0? ('draft/'+mail.temp): mail.id));
                    // }
                    // }
                    //>
                    <tr>
                        <td>{contact.name}</td>
                        <td>{contact.userEmail}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button  className="btn btn-primary btn-lg btn-block position-relative"
                     style={{ marginLeft: '85%', marginTop: '-2px', fontSize:'1.1rem'}}>
            Add Contact
            </button>
        </div>
    );
}

export default ContactBox;