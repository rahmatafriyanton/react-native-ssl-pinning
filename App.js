import axios, {AxiosResponse} from 'axios';
import React, {useState} from 'react';
import {Button, StyleSheet, Text, View} from 'react-native';

// Definisikan tipe untuk state 'message'
const App = () => {
  const [message, setMessage] = useState('test'); // Tipe untuk state message adalah string

  // Menggunakan AxiosResponse untuk mendefinisikan tipe respons
  const fetchData = async () => {
    try {
      // Gunakan AxiosResponse untuk tipe response
      const response: AxiosResponse = await axios.get(
        `https://jsonplaceholder.typicode.com/todos/${randomInt(1, 9)}`,
      );
      console.log('API Response:', response.data);

      // Perbarui state dengan pesan yang mencakup title dari response API
      setMessage(`API Request Successful! \n\n Title: ${response.data.title}`);
    } catch (error) {
      setMessage('API Request Failed!');
      console.error('API Error:', error);
    }
  };

  const randomInt = (min: number, max: number): number => {
    return Math.floor(Math.random() * (max - min + 1)) + min;
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
