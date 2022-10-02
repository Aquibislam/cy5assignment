import { Component } from "react";
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

const GROUP_DATA_API = 'http://localhost:8080/groups';
const USER_DATA_API = 'http://localhost:8080/users';

class UserData extends Component{
    
    constructor(props){
        super(props);
        this.state = {
         users:undefined,
         groups:undefined,
         modalData: {
            show: false,
            heading:undefined,
            bodyData:{
                type: undefined,
                userArn: undefined,
                groups: undefined
            }
         }
        };
      }

    componentDidMount(){
        this.fetchUsersFromBackend().then((data) => {
            this.setState({ users: data })
          })
          .catch(function (ex) {
              console.log('Response parsing failed. Error: ', ex);
          });

          this.fetchGroupsFromBackend().then((data) => {
            this.setState({ groups: data })
          })
          .catch(function (ex) {
              console.log('Response parsing failed. Error: ', ex);
          });
    }

    fetchUsersFromBackend = () =>{
        return fetch(USER_DATA_API,{ 
           
            method: 'get',
                headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json',
                },
                'credentials': 'same-origin'
        })
        .then(res => res.json());        
    }

    fetchGroupsFromBackend = () =>{
        return fetch(GROUP_DATA_API,{ 
           
            method: 'get',
                headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json',
                },
                'credentials': 'same-origin'
        })
        .then(res => res.json());        
    }

    onUserClick = (usersData) => {
        this.setState({...this.state,
            modalData: {
                show: true,
                heading: <p>Details for user "<strong>{usersData.userName}</strong>"</p>,
                bodyData:{
                    type: "userData",
                    userArn: usersData.userArn,
                    groups: usersData.groups
                }
             }
        })
    }

    onGroupClick = (groupData) => {
        this.setState({...this.state,
            modalData: {
                show: true,
                heading: <p>Details for group "<strong>{groupData.groupName}</strong>"</p>,
                bodyData:{
                    type:"groupData",
                    groupArn: groupData.groupArn,
                    users: groupData.userDataList
                }
             }
        })
    }

    handleModalClose = () =>{
        this.setState({...this.state,
            modalData: {
                show: false,
                heading:undefined,
                bodyData:undefined
             }
        })
    }

    getModalBody = () =>{
        if(this.state.modalData.show){
        
        if(this.state.modalData.bodyData.type=="userData"){
           let groupData=[];
           let groupLength=this.state.modalData.bodyData.groups?this.state.modalData.bodyData.groups.length:0;
           let groupsBodyData=this.state.modalData.bodyData.groups;
           for(let i=0;i<groupLength;i++){
            groupData.push(
                <tr>
                  <td>{groupsBodyData[i].groupName}</td>
                  <td>{groupsBodyData[i].groupArn}</td>                    
                </tr>
            )
           }
           return( 
            <div>
             <hr/>
             <p><strong>UserArn</strong> : {this.state.modalData.bodyData.userArn}</p>
             <hr/>
             <h3>User's Groups</h3>
            <table className="table table-striped">
                <thead>
                <tr>
                    <th>Groups Name</th>
                    <th>Groups Arn</th>
                </tr>                           
                </thead>
                <tbody>
                    {groupData}
                </tbody>
           </table>
           </div>)
        }
        else{
            let userData=[];
            let usersLength=this.state.modalData.bodyData.users?this.state.modalData.bodyData.users.length:0;
            let usersBodyData=this.state.modalData.bodyData.users;
            for(let i=0;i<usersLength;i++){
             userData.push(
                 <tr>
                   <td>{usersBodyData[i].userName}</td>
                   <td>{usersBodyData[i].userArn}</td>                    
                 </tr>
             )
            }
            return( 
             <div>
              <hr/>
              <p><strong>GroupArn</strong> : {this.state.modalData.bodyData.groupArn}</p>
              <hr/>
              <h3>Users in this Groups</h3>
             <table className="table table-striped">
                 <thead>
                 <tr>
                     <th>User Name</th>
                     <th>User Arn</th>
                 </tr>                           
                 </thead>
                 <tbody>
                     {userData}
                 </tbody>
            </table>
            </div>) 

        }
    }

       
    }
    
    
   
    render = ()=>{
        let usersLength=this.state.users?this.state.users.length:0;
        let usersList=[];
       
        if(usersLength>0){
            let userData=this.state.users;
            
            for(let i=0;i<usersLength;i++){
                usersList.push(<tr key={i}><td><a href="#" onClick={()=>this.onUserClick(userData[i])}>{userData[i].userName}</a></td></tr>)           
            }
        }

        let groupsLength=this.state.groups?this.state.groups.length:0;
        let groupsList=[];
        if(groupsLength>0){
            let groupsData=this.state.groups;
            for(let i=0;i<groupsLength;i++){
                groupsList.push(<tr key={i}><td><a href="#" onClick={()=>this.onGroupClick(groupsData[i])}>{groupsData[i].groupName}</a></td></tr>)
               
            }
        }

        return (
            <div>
             <div>
                <h1>Users</h1>
                <table className="table table-striped">
                        <thead>
                           <tr>
                             <th>UserName</th>
                          </tr>                           
                        </thead>
                        <tbody>
                            {usersList}
                        </tbody>
                </table>
             </div>
             <div>
              <h1>Groups</h1>
              <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>GroupName</th>
                            </tr>
                        </thead>
                        <tbody>
                          {groupsList}
                        </tbody>
                </table>
             </div>



            

             
             <Modal size="lg" show={this.state.modalData.show} onHide={this.handleModalClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{this.state.modalData.heading}</Modal.Title>
                </Modal.Header>
             <Modal.Body>
              {this.getModalBody()}
             </Modal.Body>
             <Modal.Footer>
                    <Button variant="secondary" onClick={this.handleModalClose}>
                        Close
                    </Button>
             </Modal.Footer>
             </Modal>
            </div>
        )
    }
}
export default UserData;