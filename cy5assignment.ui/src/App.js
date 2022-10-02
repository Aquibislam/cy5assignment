import React, { Component } from 'react';
import './App.css';
import Cy5Bar from './Components/Cy5Bar';
import 'bootstrap/dist/css/bootstrap.min.css';
import UserData from './Components/UserData';


class App extends Component{
  

  render(){

    return (
      <div>
       <div>
        <Cy5Bar />
       </div>
       <div>
        <UserData />
       </div>
      </div>
    );
  }
}
export default App;