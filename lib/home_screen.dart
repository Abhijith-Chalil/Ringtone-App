import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final channel = const MethodChannel("ringtone_channel");
  List? ringtones = [];
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Ringtone Player"),
        backgroundColor: Colors.amber,
        centerTitle: true,
      ),
      body: Column(
        children: [
          if (ringtones != null && ringtones!.isNotEmpty)
            Flexible(
              child: ListView.separated(
                  itemBuilder: (context, index) {
                    final ringtoneName = ringtones![index]["title"];
                    final ringtoneUrl = ringtones![index]["url"];
                    return ListTile(
                      title: Text(ringtoneName),
                      onTap: () async {
                        channel.invokeMethod("play", ringtoneUrl);
                      },
                    );
                  },
                  separatorBuilder: (context, index) => const Divider(),
                  itemCount: ringtones!.length),
            ),
          Align(
            alignment: Alignment.center,
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: ElevatedButton(
                  onPressed: () async {
                    ringtones = await channel.invokeMethod("getRingtones");
                    setState(() {});
                  },
                  child: const Text("Get Ringtones")),
            ),
          )
        ],
      ),
    );
  }
}
