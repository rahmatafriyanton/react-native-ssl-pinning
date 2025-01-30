import axios from 'axios';
import React, {useState} from 'react';
import {Button, StyleSheet, Text, View} from 'react-native';

const App = () => {
  const [message, setMessage] = useState('test');

  const fetchData = async () => {
    try {
      const response = await axios.get('https://dummyjson.com/user/login');
      console.log('API Response:', response.data);
      setMessage(`API Request Successful! Title: ${response.status}`);
    } catch (error) {
      console.error('API Error:', error);
      setMessage('API Request Failed!');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>SSL Pinning Example</Text>
      <Text style={styles.message}>{message}</Text>
      <Button title="Fetch Data" onPress={fetchData} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },
  title: {
    fontSize: 24,
    marginBottom: 16,
  },
  message: {
    fontSize: 16,
    marginBottom: 16,
  },
});

export default App;
