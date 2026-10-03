package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

data class SoundTrack(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String, // "موسيقى هادئة", "أصوات الطبيعة", "ترددات السكينة"
    val iconEmoji: String,
    val benefits: String,
    val bpmOrFrequency: String
)

object CalmAudioEngine {
    private var audioTrack: AudioTrack? = null
    @Volatile
    private var isPlaying = false
    private var currentMode = "music_piano"

    private val scope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null

    private val _isPlayingFlow = MutableStateFlow(false)
    val isPlayingFlow: StateFlow<Boolean> = _isPlayingFlow.asStateFlow()

    private val _currentTrackFlow = MutableStateFlow<SoundTrack?>(null)
    val currentTrackFlow: StateFlow<SoundTrack?> = _currentTrackFlow.asStateFlow()

    private val _masterVolumeFlow = MutableStateFlow(0.85f)
    val masterVolumeFlow: StateFlow<Float> = _masterVolumeFlow.asStateFlow()

    private val _remainingTimerSeconds = MutableStateFlow<Int?>(null)
    val remainingTimerSeconds: StateFlow<Int?> = _remainingTimerSeconds.asStateFlow()

    val availableTracks = listOf(
        SoundTrack(
            id = "music_piano",
            title = "بيانو السكينة والألحان الدافئة",
            subtitle = "نغمات بيانو تأملية متسلسلة بتدرج سلم دوريان الهادئ",
            category = "موسيقى هادئة",
            iconEmoji = "🎹",
            benefits = "تهدئة التفكير المفرط، تصفية الذهن، وتحسين التركيز",
            bpmOrFrequency = "56 نبضة • 432 Hz"
        ),
        SoundTrack(
            id = "music_harp",
            title = "أوتار القيثارة وانسياب النهر",
            subtitle = "عزف قيثارة خماسي رخيم مع ارتداد صوتي ناعم",
            category = "موسيقى هادئة",
            iconEmoji = "🪕",
            benefits = "استرخاء عضلات الكتف، خفض التوتر اليومي، والهدوء",
            bpmOrFrequency = "انسياب حر • 432 Hz"
        ),
        SoundTrack(
            id = "music_zen_bowl",
            title = "طنين أوعية السكينة التبتية",
            subtitle = "ترددات نقية متناسقة مع التردد الطبيعي لجسم الإنسان",
            category = "ترددات السكينة",
            iconEmoji = "🔔",
            benefits = "التأمل العميق، تهدئة ضربات القلب، واستعادة التوازن",
            bpmOrFrequency = "رنين توافقي • 432 Hz"
        ),
        SoundTrack(
            id = "binaural_sleep",
            title = "موجات دلتا للنوم العميق والراحة",
            subtitle = "نبضات خافتة تحاكي حالة النوم العميق وتفريغ الأرق",
            category = "ترددات السكينة",
            iconEmoji = "🌙",
            benefits = "مقاومة الأرق، نوم عميق ومريح، وسكون داخلي",
            bpmOrFrequency = "موجات دلتا • 3 Hz"
        ),
        SoundTrack(
            id = "rain",
            title = "مطر هادئ على زجاج النافذة",
            subtitle = "صوت انهمار خفيف ومتواصل مع قطرات مطر مهدئة",
            category = "أصوات الطبيعة",
            iconEmoji = "🌧️",
            benefits = "عزل الضوضاء الخارجية، شعور بالأمان، وسكينة منزلية",
            bpmOrFrequency = "ضوضاء وردية طبيعية"
        ),
        SoundTrack(
            id = "waves",
            title = "أمواج البحر والمد الهادئ",
            subtitle = "حركة مد وجزر متسقة تماماً مع وتيرة التنفس العميق",
            category = "أصوات الطبيعة",
            iconEmoji = "🌊",
            benefits = "تنظيم وتيرة التنفس، تفريغ الشحنات السلبية، والراحة",
            bpmOrFrequency = "دورة كل 8 ثوانٍ"
        ),
        SoundTrack(
            id = "forest",
            title = "نسيم الغابة وزقزقة الصباح",
            subtitle = "حفيف أوراق شجر ناعم وأصوات طيور مغردة من بعيد",
            category = "أصوات الطبيعة",
            iconEmoji = "🌿",
            benefits = "الاتصال بالطبيعة، الانتعاش الذهني، وبدء يوم هادئ",
            bpmOrFrequency = "أجواء حية"
        ),
        SoundTrack(
            id = "fireplace",
            title = "دفء الموقد وفرقعات الخشب",
            subtitle = "أصوات نار خافتة تبعث على الاستكانة والدفء الروحي",
            category = "أصوات الطبيعة",
            iconEmoji = "🪵",
            benefits = "الراحة قبل النوم، الاسترخاء، وخلق مساحة أمان",
            bpmOrFrequency = "ترددات دافئة"
        )
    )

    fun start(mode: String) {
        stop()
        currentMode = mode
        val trackInfo = availableTracks.find { it.id == mode } ?: availableTracks[0]
        _currentTrackFlow.value = trackInfo
        isPlaying = true
        _isPlayingFlow.value = true

        Thread {
            try {
                val sampleRate = 22050
                val minBuffer = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val bufferSize = minBuffer.coerceAtLeast(4096)

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.play()

                val buffer = ShortArray(bufferSize / 2)
                val random = Random()

                // Synthesis State Variables
                var b0 = 0.0
                var b1 = 0.0
                var b2 = 0.0
                var phase = 0.0
                var lfoPhase = 0.0
                var lfoPhase2 = 0.0

                // Music Chord Progression Frequencies (Hz): C Maj9 -> G Sus -> Am7 -> F Maj7
                val pianoChords = listOf(
                    listOf(261.63, 329.63, 392.00, 493.88), // Cmaj7
                    listOf(196.00, 261.63, 293.66, 392.00), // Gsus
                    listOf(220.00, 261.63, 329.63, 392.00), // Am7
                    listOf(174.61, 220.00, 261.63, 349.23)  // Fmaj7
                )
                var currentChordIndex = 0
                var sampleCounter = 0L
                val chordLengthSamples = sampleRate * 4L // 4 seconds per chord

                // Harp Arpeggio Notes
                val harpNotes = listOf(261.63, 293.66, 329.63, 392.00, 440.00, 523.25, 440.00, 392.00)
                var harpNoteIndex = 0
                val harpNoteDuration = (sampleRate * 0.45).toInt()
                var harpSampleCounter = 0

                while (isPlaying) {
                    val volume = _masterVolumeFlow.value.coerceIn(0f, 1f)

                    for (i in buffer.indices) {
                        sampleCounter++
                        val sample = when (currentMode) {
                            "music_piano" -> {
                                if (sampleCounter % chordLengthSamples == 0L) {
                                    currentChordIndex = (currentChordIndex + 1) % pianoChords.size
                                }
                                val chord = pianoChords[currentChordIndex]
                                val timeInChord = (sampleCounter % chordLengthSamples).toDouble() / sampleRate
                                val envelope = exp(-timeInChord * 0.75) * 0.9 + 0.1

                                var chordWave = 0.0
                                for ((idx, freq) in chord.withIndex()) {
                                    val wave = sin(2.0 * PI * freq * (sampleCounter.toDouble() / sampleRate))
                                    val harmonic = sin(4.0 * PI * freq * (sampleCounter.toDouble() / sampleRate)) * 0.25
                                    chordWave += (wave + harmonic) * (1.0 / (idx + 1))
                                }
                                val output = chordWave * envelope * 5500 * volume
                                output.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                            "music_harp" -> {
                                harpSampleCounter++
                                if (harpSampleCounter >= harpNoteDuration) {
                                    harpSampleCounter = 0
                                    harpNoteIndex = (harpNoteIndex + 1) % harpNotes.size
                                }
                                val freq = harpNotes[harpNoteIndex]
                                val noteProgress = harpSampleCounter.toDouble() / harpNoteDuration
                                val pluckEnvelope = exp(-noteProgress * 4.5)
                                val wave = sin(2.0 * PI * freq * (harpSampleCounter.toDouble() / sampleRate))
                                val harmonic = sin(4.0 * PI * freq * (harpSampleCounter.toDouble() / sampleRate)) * 0.35
                                val output = (wave + harmonic) * pluckEnvelope * 11000 * volume
                                output.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                            "music_zen_bowl" -> {
                                lfoPhase += 2.0 * PI * 0.15 / sampleRate
                                val tremolo = (sin(lfoPhase) * 0.2) + 0.8
                                val fundamental = sin(2.0 * PI * 432.0 * (sampleCounter.toDouble() / sampleRate))
                                val overtone1 = sin(2.0 * PI * 864.0 * (sampleCounter.toDouble() / sampleRate)) * 0.28
                                val overtone2 = sin(2.0 * PI * 1296.0 * (sampleCounter.toDouble() / sampleRate)) * 0.12
                                val bowlWave = (fundamental + overtone1 + overtone2) * tremolo * 8000 * volume
                                bowlWave.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                            "binaural_sleep" -> {
                                lfoPhase += 2.0 * PI * 0.05 / sampleRate
                                val baseFreq = 144.0 // Calm fundamental
                                val beatOffset = 3.0 // 3 Hz Delta rhythm
                                val carrier = sin(2.0 * PI * baseFreq * (sampleCounter.toDouble() / sampleRate))
                                val modulator = sin(2.0 * PI * (baseFreq + beatOffset) * (sampleCounter.toDouble() / sampleRate))
                                val output = (carrier * 0.5 + modulator * 0.5) * 8500 * volume
                                output.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                            "waves" -> {
                                lfoPhase += 2.0 * PI * 0.11 / sampleRate
                                lfoPhase2 += 2.0 * PI * 0.055 / sampleRate
                                val swell = ((sin(lfoPhase) + sin(lfoPhase2) * 0.4) + 1.4) * 0.45
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                b0 = 0.985 * b0 + white * 0.05
                                val value = b0 * swell * 17000 * volume
                                value.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                            "forest" -> {
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                b0 = 0.98 * b0 + white * 0.06
                                var birdChirp = 0.0
                                // Sporadic sweet avian chirps
                                if (random.nextInt(32000) == 7) {
                                    phase = 0.0
                                }
                                if (phase < 600) {
                                    phase++
                                    birdChirp = sin(2.0 * PI * (2400.0 + sin(phase * 0.1) * 400.0) * (phase / sampleRate)) * 0.25
                                }
                                val value = (b0 * 6000 + birdChirp * 5000) * volume
                                value.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                            "fireplace" -> {
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                b0 = 0.96 * b0 + white * 0.08
                                var crackle = 0.0
                                if (random.nextInt(600) < 4) {
                                    crackle = (random.nextDouble() * 2.0 - 1.0) * 12000
                                }
                                val value = (b0 * 5000 + crackle) * volume
                                value.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                            else -> { // "rain"
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                b0 = 0.99 * b0 + white * 0.055
                                b1 = 0.95 * b1 + white * 0.075
                                b2 = 0.85 * b2 + white * 0.15
                                var drop = 0.0
                                if (random.nextInt(1200) < 3) {
                                    drop = (random.nextDouble() * 2.0 - 1.0) * 4000
                                }
                                val pink = ((b0 + b1 + b2) * 2200 + drop) * volume
                                pink.coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                        }
                        buffer[i] = sample
                    }
                    track.write(buffer, 0, buffer.size)
                }
                track.stop()
                track.release()
            } catch (_: Exception) {
            }
        }.start()
    }

    fun stop() {
        isPlaying = false
        _isPlayingFlow.value = false
        _currentTrackFlow.value = null
        cancelTimer()
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }

    fun setVolume(vol: Float) {
        _masterVolumeFlow.value = vol.coerceIn(0f, 1f)
    }

    fun setTimer(minutes: Int) {
        cancelTimer()
        if (minutes <= 0) {
            _remainingTimerSeconds.value = null
            return
        }

        var remaining = minutes * 60
        _remainingTimerSeconds.value = remaining

        timerJob = scope.launch {
            while (remaining > 0 && isPlaying) {
                delay(1000)
                remaining--
                _remainingTimerSeconds.value = remaining
            }
            if (remaining <= 0) {
                stop()
            }
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
        _remainingTimerSeconds.value = null
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying

    fun getActiveMode(): String = currentMode
}
