import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import AddContact from "./AddContact.jsx";
import {FaPlus} from "react-icons/fa";
import {FiSearch} from "react-icons/fi";
import {SlReload} from "react-icons/sl";


function ContactBox() {
    const navigate = useNavigate();
    const params = useParams();
    const [contactBox, setContactBox] = useState(false);

    const [allContacts,setAllContacts] = useState([])



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

            await axios.delete(url);
            window.location.reload();
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
                setAllContacts(sorted)
                console.log("here")
                console.log(sorted)
            } catch (error) {
                console.error('Error fetching emails:', error);
            }
        }

        fetchMails();
    }, [params.user]);

    function editClicked(e){
        const i = e.target.value
        console.log(editFlag)
        setEditFlag(!editFlag)
        console.log(editFlag)
        console.log(i)
        setContactToEdit(() => contact.filter((c) => c.name === i))
        console.log(contactToEdit)

    }

    function editedContact(i){
        return <AddContact name={i.name} email={i.userEmails} edit={true} onClick={() => setEditFlag(false)}/>

    }

    const [contactToEdit,setContactToEdit] = useState({})

    const [editFlag, setEditFlag] = useState( false);

    function search(e) {
        console.log("test")
        console.log(allContacts)

        const searched = [...allContacts].filter(contact => (contact.name.toLowerCase().includes(e.target.value.toLowerCase()) ));
        //const x = [...searched].filter((c) => c.userEmails.filter( s => s.toLowerCase().includes(e.target.value.toLowerCase()) ));

        setContacts(searched)
        //setNumberOfPages(Array(res.data.length).fill(1))

    }

    return (
        <div className="Contact-box">

            <div className={"list-title"}>

                <div style={{display: "flex"}}>
                    <h1 style={{fontSize: "3rem", fontWeight: "normal"}}>Contacts</h1>

                </div>

            </div>
            <hr/>
            <div>
                <label><FiSearch style={{fontSize: "1.5rem"}}/></label>
                <input className={"search-input"} type={"text"} placeholder={"search..."} onChange={search}/>
            </div>


            <table className="table list-box-table table-hover contact-table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                {contact.map((c, index) => (
                    <tr key={index}>
                        <td>{c.name}</td>
                        <td>{c.userEmails}</td>
                        <td className={"table-btns"}>
                            <button
                                className={"btn "}
                                onClick={() => navigate("/" + params.user + "/sendContact" + "/" + c.userEmails)}
                            >
                                <FaPlus/>
                            </button>
                            <button className={"btn"} value={c.name} onClick={DeleteContact}>delete</button>
                            <button className={index + " btn "} value={c.name} onClick={editClicked}>Edit</button>
                        </td>
                        <td>{editFlag && editedContact(contactToEdit[0])}</td>
                    </tr>

                ))}
                </tbody>
            </table>


            <button className="btn btn-primary btn-lg btn-block position-relative"
                    style={{marginLeft: '85%', marginTop: '-2px', fontSize: '1.1rem'}}
                    onClick={() => setContactBox(!contactBox)}
            >
                Add Contact
            </button>
            {contactBox && <AddContact name={""} email={[]} edit={false} onClick={() => setContactBox(false)}/>}
        </div>
    );
}

export default ContactBox;