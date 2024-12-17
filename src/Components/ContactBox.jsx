import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import AddContact from "./AddContact.jsx";


function ContactBox() {
    const navigate = useNavigate();
    const params = useParams();
    const [contactBox, setContactBox] = useState(false);



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

    async function DeleteContact(e) {
        try {
            const param = {
                name: e.target.value ,
                email: params.user
            };

            console.log(params.user);

            const url =
                `http://localhost:8080/api/users/deleteContact/${param.name}/${param.email}`;

            await axios.post(url);
        } catch (error) {
            console.error('Error deketing contact', error);
        }
    }

    useEffect(() => {
        async function fetchMails() {
            try {

                const data = await axios.get(`http://localhost:8080/api/users/getContacts/${params.user}`)
                console.log(data.data);
                const sorted = [...data.data].sort((a, b) => a.name.toLowerCase().localeCompare(b.name.toLowerCase()));
                setContacts(sorted);
            } catch (error) {
                console.error('Error fetching emails:', error);
            }
        }

        fetchMails();
    }, [params.user]);


    return (
        <div className="Contact-box">
          
            <table className="table table-striped list-box-table table-hover">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                {contact.map((c,index) => (
                    <tr key={index} >
                        <td>{c.name}</td>

                        <td>{c.userEmails}</td>

                        <button onClick={() => navigate("/" + params.user + "/sendContact"+"/"+ c.userEmails)}>+</button>
                        <button value={c.name} onClick={DeleteContact}>delete</button>
                    </tr>
                ))}
                </tbody>
            </table>
            <button  className="btn btn-primary btn-lg btn-block position-relative"
                     style={{ marginLeft: '85%', marginTop: '-2px', fontSize:'1.1rem'}}
                     onClick={() => setContactBox(!contactBox)}
            >
            Add Contact
            </button>
            {contactBox && <AddContact/>}
        </div>
    );
}

export default ContactBox;